
   document.getElementById("listar-videos").addEventListener("click", async function(event) {
       event.preventDefault();
       await mostrarVideo(); // Suponiendo que el ID del video es 1
   });


   async function mostrarVideo() {
       try {
           const response = await fetch(`/employees`);
           if (!response.ok) {
               throw new Error('Error al obtener el video');
           }

           // Suponiendo que el videoData.video contiene los datos del video en base64
           const videoDTO = await response.json();
           const videoData = videoDTO.video;

           // Crear una URL de datos a partir de los datos en base64
           const videoSrc = `data:video/mp4;base64,${videoData}`;

           const contentDiv = document.getElementById("content");

           const video = document.createElement('video');
           video.width = 640;
           video.height = 480;
           video.controls = true;
           video.src = videoSrc;

           contentDiv.appendChild(video);
       } catch (error) {
           console.error('Error al obtener el video:', error);
       }
   }



