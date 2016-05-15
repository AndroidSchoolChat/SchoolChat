# SchoolChat
el registro no esta terminado

ggordo12 CAMBIOS GABRIEL:

-He añadido el context de FireBase para que se enlacen correctamente.

-He cambiado el dato que aparece al crear un usuario, en este caso en vez de el UID aparecera su nombre de usuario

main activity funcional pero hay que editar el archivo perfil.xml para la interfaz(ver demostracion)

chat no funciona
-Diseño de la actividad AltaUsuario cambiado:
         *Se ha añadidod correctamente la verificacion de la contraseña del profesor

 FALTA: Correcciones menores, debido a que FIREBASE esta registrando varias veces el mismo usuario debido a que cada vez
      que se añade un nuevo profesor el metodo DATACHANGE se activa (ya que los datos han sido modificado) y esto implica que intenta añadir de nuevo al mismo usuario provocando que se muestr el error de que el usuario ya existe.

      Esto hace que se meustre un error indicando que se intenta mostrar un alertDialog cuando la actividad ya esta   cerrada,   esto es, debido a que una vez creado el profesor nuevo se manda a la pagina login lo que provoca que este alertDialog intente aparecer y provoque el error indicado.

      SOLUCION: Intentar modificar cuando haga el snapshot y que no sea en DataChanged.

 NOTA:
      *INTENTAR AÑADIR EL TITULO DE LA ACTIVIDAD
      *Al girar el AVD se desordena el layout
      *Al escribir un nombre de usuario muy largo el layout se desordena
la funcion de chat ya esta disponible
ahora aparecen el nombre de usuario y su estado en el main
la interface del main ha sido cambiada, ahora se diferencia los recuadros de los usuarios

para corregir errores:
se han cambiado nombre de variables
      los moldes han de tener las variables que la estrucutura de firebase y empezar con minusculas debido a esto los usuarios creado han sido borrados al crearse de forma incorrrecta con campos que provocaban los errores
      
para cambiar el estado se conexion solo se hace en la rama alumnos, si un profesor se conecta no se cambia en la rama profesor sino que copia su uid en rama alumnos  con un unico campo "conexion" que si se selecciona en el main producira un recuadro vacio que al seleccionarlo produce un error
