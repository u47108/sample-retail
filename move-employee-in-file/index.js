'use strict';

const ftpConfig = {
    "host"          : process.env.FTP_EMPLOYEE_HOST,
    "username"      : process.env.FTP_EMPLOYEE_USERNAME,
    "port"          : process.env.FTP_EMPLOYEE_PORT,
    "path"          : process.env.FTP_EMPLOYEE_PATH,
    "privateKey"    : process.env.FTP_EMPLOYEE_KEYNAME
}

const version = '1.0.0';

// Logger
const bunyan = require('bunyan');
const {LoggingBunyan} = require('@google-cloud/logging-bunyan');
const loggingBunyan = new LoggingBunyan();
const logger = bunyan.createLogger({
    name: 'employee-move-in-file',
    streams: [
        {stream: process.stdout, level: 'info'},
        loggingBunyan.stream('info')
    ],
});

const {Storage} = require('@google-cloud/storage');
const storage = new Storage();

const fs = require('fs');
const kms = require('@google-cloud/kms');
/**
 * Functions exposed to GCP.
 * @param {object} event The Cloud Functions event.
 * @param {function} callback The callback function.
 */
exports.moveFileFTPEmployee = (data,context, callback) => {

    const file = data;
    logger.info('--------------------------------------');
    logger.info(`init [moveFile] ${version}`);
    logger.info(`fileName: ${file.body}`);
    const fileName= file.body;
if (!file.body) {
        logger.error('File don\'t exists');
        callback(new Error('File don\'t exists'));
        return;
    }
    try {
        writeTemp(file)
            .then(() => {
                moveToFTP(fileName, callback)
                    .then(() => {
                        callback();
                        logger.info(`end [moveFile] ${version}`);
                        return;
                    })
                    .catch(err => {
                        logger.error('moveToFTP - catch - ERROR: ' + err);
                        console.log(err);
                        logger.info(`end [moveFile] ${version}`);
                        return;
                    });
            })
            .catch(err => {
                logger.error('writeTemp - catch: ' + err);
                console.log(err);
                logger.info(`end [moveFile] ${version}`);
                return;
            });
    } catch (err) {
        logger.error('Error: ' + err);
         callback(null, 'Error!');
        logger.info(`end [moveFile] ${version}`);
        return;
    }
}


/**
 * 
 * @param {object} event The Cloud Functions event.
 */
function moveToFTP(file) {
    logger.info(`> init [moveToFTP] ${version}`);
    logger.info(`fileName: ${file}`);
    return new Promise((resolve, reject) => {
        try {
           
                    let config = {
                        "host"          : ftpConfig.host,
                        "port"          : ftpConfig.port,
                        "username"      : ftpConfig.username
                    };
                    var Client = require('ssh2-sftp-client');
                    var sftp = new Client();  

                    const tempFile = `/tmp/temp_${file}`;
                    const targetFile = `${ftpConfig.path}/${file}`;      

                    sftp.connect(config).then(() => {
                        logger.info('connection is ready to upload...');                       
                        sftp.fastPut(tempFile, targetFile, null, (err) => {
                            if(err) {
                                var erro = 'Error when move to ftp: ' + err;
                                logger.info(erro);
                                reject(erro);
                                return;
                            }                
                        });
                        logger.info(`end upload [moveToFTP] ${version}`);
                        resolve();
                        return;
                    }).catch((err) => {
                        logger.error('sftp-error -> ', err);
                        reject(err);
                        return;
                    });
                
        } catch (err) {
            var erro = 'Error when move to ftp: ' + err;
            logger.error(erro);
            reject(erro);
            return;
        }
    });
};

/**
 * This functionts obtain stream of a file in cloud storage bucket
 * @param {Object} file Object with info of file trigger
 * @param {Function} callback Function call after function end
 */
function writeTemp (file) {
    logger.info(`init [writeTemp] ${version}`);
    return new Promise((resolve, reject) => {
        try {
            const targetFile = `/tmp/temp_${file.body}`;
            const sourceStream = getFileStream(file.body);
            const tempStream = fs.createWriteStream(targetFile);
            tempStream.on('finish', function() {
                logger.info(`end [writeTemp] ${version}`);
                if(fs.existsSync(targetFile)) {
                    logger.info('file save successful');
                    resolve();
                    return;
                } else {
                    logger.error('no se ha creado el archivo temporal');
                    reject(new Error('filetemp cant created'));
                    return;
                }
            });
            sourceStream.pipe(tempStream);
        } catch (err) {
            let erro = 'Error when write temp: ' + err;
            logger.error(erro);
            reject(err);
            return;
        } 
    });
};

/**
 * This functionts obtain stream of a file in cloud storage bucket
 * @param {Object} file 
 */
function getFileStream (file) {
   
    return storage.bucket('employee-files-in').file(file).createReadStream();
};

/**
 * Obtener el secreto desde cloud storage y desencriptar con kms
 * @param {*} secretName 
 */
function getSecretValue(secretName){
    logger.info(`> init [getSecretValue] ${version}`);
    return new Promise((resolve, reject) => {
        getSecretEncrypt(secretName)
            .then(encryptedValue => {
                if(!encryptedValue){
                    throw new Error('Error when decryptSecret');
                }
                decryptKMS(encryptedValue)
                    .then(secretValue => {
                        resolve(secretValue);
                    })
                    .catch(err => {
                        reject(err);                        
                    });
            })
            .catch(err => {
                reject(err);
            });
    });
};

function decryptKMS(cipherText) {
    logger.info(`> init [decryptKMS] ${version}`);
    return new Promise((resolve, reject) => {        
        const client = new kms.v1.KeyManagementServiceClient({});
        const formattedName = client.cryptoKeyPath(process.env.GCP_PROJECT, 'global', 'encrypted-credentials', 'credentials-keys');
        const request = {
            name: formattedName,
            ciphertext: cipherText,
        };
        client.decrypt(request)
            .then(responses => {
                const response = responses[0];
                resolve(response.plaintext.toString('utf8'));
                return;
            })
            .catch(err => {
                reject(err);
                return;
            });
    });
}

function getSecretEncrypt(secretName) {
    logger.info(`> init [getSecretEncrypt] ${version}`);
    logger.info(`> secretName ${secretName}`);
    return new Promise((resolve, reject) => {
        const tempFile = `/tmp/${secretName}.encrypted`;
        if (fs.existsSync(tempFile)) {
            resolve(fs.readFileSync(tempFile).toString('base64'));
            return;
        }
        try {
            const bucketName = process.env.GCP_PROJECT + '-encrypted-secrets';
            logger.info(`> bucketName ${bucketName}`);
            let otherFile = {
                "bucket"    : bucketName, 
                "name"      :`${secretName}.encrypted`
            };
            const sourceStream = getFileStream(otherFile);
            const tempStream = fs.createWriteStream(tempFile);
            tempStream.on('finish', function() {
                resolve(fs.readFileSync(tempFile).toString('base64'));
                return;
            });
            sourceStream.pipe(tempStream);
        } catch (err) {
            reject('Error when write temp: ' + err);
            return;
        } 
    });
}
