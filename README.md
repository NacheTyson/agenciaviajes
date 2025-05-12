Una agencia de viajes ofrece a sus clientes paquetes vacacionales. Un paquete vacacional está formado por un identificador numérico asignado por el programa automáticamente de forma que no haya repetidos, ciudad de origen, ciudad de destino, transporte, alojamiento y una lista de extras, además de la fecha (será un String y no será necesario comprobar que sea correcta) de salida y de la duración del viaje expresada en días. De un transporte debemos conocer la duración del trayecto (se asume que la duración es la misma que la de la vuelta) expresada en minutos y el precio. Tendremos dos tipos de transporte: avión o tren. En el caso del transporte en avión almacenaremos si el vuelo es directo o no.
Del alojamiento se guardará el nombre, distancia en metros al centro de la ciudad y precio por día.
Los extras almacenarán una descripción y un precio. Los paquetes vacacionales tendrán un método precioTotal que nos indicará el precio total del vuelo+alojamiento+extras.
Se desea implementar un programa que gestione una lista de paquetes vacacionales y que nos permita las siguientes operaciones:
1. Crear paquete vacacional: Creará un nuevo paquete vacacional pidiendo los datos
necesarios y preguntando por todos los extras que se quiera añadir al paquete. 2. Mostrar paquetes vacacionales: Mostrará por pantalla todos los paquetes
vacacionales creados con el precio total de cada uno de ellos y también guardará en un fichero de texto llamado paquetes.txt ese mismo listado.
3. Mostrar paquetes con filtro: Mostrar aquellos paquetes cuya duración por trayecto
en el transporte no sea superior a la indicada por el usuario y que además no incluyan vuelos con escalas
4. Eliminar paquete vacacional: eliminará del listado el paquete cuyo identificador
indique el usuario (si el identificador dado no existe lo indicará mediante un mensaje)
5. Salir
Al salir del programa se almacenará la situación actual del programa de forma que al volver a iniciar el programa se recupere los paquetes vacacionales creados.
Se deberá controlar todas las posibles excepciones.
