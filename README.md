# sample-retail
sample en GCP de microservicios con cloudfunction y pubsub

![Arquitectura GCP](/arquitectura-gcp.png?raw=true "arquitectura")
     Spring Boot los microservicios detallados en la arquitectura arriba mostrada.
   ##  Microservicio 1- Employees:
         - Consumir web servicedummyemployeeshttp://dummy.restapiexample.com/api/v1/employees
         - Generar un CSV y dejarlo en SFTP server ---> OK
   ##  Microservicio 2 - Cleansing:
        - Escuchar mensajes de pubsub que indiquen en el mensaje un bucket file del GCS.
         - { body: “<archivo_gcs>” }
            
    * aca hay una error de descripción y de flujo primero que nada no hay microservicio que escriba en el bucket file,*
    * se cambio para que este servicio suba al bucket file de GCP, además de enviar el mensaje al pubsub. *
    
      - Tomar archivo (se adjunta), enmascarar u ofuscar la columna con el nombre del personaje y enviarlo al SFTP server
   ## 2. Desarrollar cloudfunction
       - Función:Trigger cuando llegue el file al bucket de cloudstorage.
    * segundo la cloudfunction es mejor dejarla con el trigger del mensaje del pubsub en ese momento existe el archivo en el store de GCP, en este proceso se puede editar y mover al SFTP con la cloudfuction *
