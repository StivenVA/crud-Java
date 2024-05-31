   document.getElementById("listar-imagenes").addEventListener("click", function(event) {
        event.preventDefault();
        mostrarContenido("Listar Imágenes", "Aquí se mostrarán las imágenes.");
    });

   document.getElementById("listar-videos").addEventListener("click", function(event) {
       event.preventDefault();
       mostrarContenido("Listar Videos", "Aquí se mostrarán los videos.");
       mostrarVideo(); // Suponiendo que el ID del video es 1
   });

    document.getElementById("crear-empleado").addEventListener("click", function(event) {
        event.preventDefault();
        mostrarContenido("Crear Empleado", "Formulario para crear un nuevo empleado.");
    });

    function mostrarContenido(titulo, contenido) {
        document.getElementById("title").innerText = titulo;

    }

   async function mostrarVideo() {
       try {
           const response = await fetch(`/employees`);
           if (!response.ok) {
               throw new Error('Error al obtener el video');
           }
           const blob = await response.blob();
           console.log(blob);

           const url = URL.createObjectURL(blob);
           console.log(url);
           window.open(url, '_blank');

           const contentDiv = document.getElementById("content");
           contentDiv.innerHTML = `
                <h1>Video</h1>
                <video width="640" height="480" controls>
                    <source src="${url}" type="video/mp4">
                    Tu navegador no soporta el elemento de video.
                </video>
            `;
       } catch (error) {
           console.error('Error al obtener el video:', error);
       }
   }

