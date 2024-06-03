document.getElementById("listar-imagenes").addEventListener("click", async function(event) {
    event.preventDefault();
    const contenido = await traerContenido("imagenes");

    document.getElementById("content").innerHTML = contenido;

    await mostrarImagenes();
});

let traerContenido = async (page) => {
    let response = await fetch(`/${page}`);

    if (!response.ok) {
        throw new Error('Error al obtener el contenido');
    }

    let contenido = await response.text();
    return contenido;
}

let mostrarImagenes = async ()=> {
    try {
        const response = await fetch(`/images`);
        if (!response.ok) {
            throw new Error('Error al obtener la imagen');
        }

        const imagesDTO = await response.json();

        console.log(imagesDTO);

        const contentDiv = document.getElementById("imagenes");

        let count = 1;
        imagesDTO.forEach(imageDTO => {
            const row = document.createElement('tr');
            const imageData = imageDTO.image;

            if (imageData!==null) {

                const imageIdTd = document.createElement("td");
                const creationDateTd = document.createElement("td");
                const creationTimeTd = document.createElement("td");
                const image = document.createElement('a');

                imageIdTd.innerHTML = `${count}`;
                creationDateTd.innerHTML = `${imageDTO.creationDate}`;
                creationTimeTd.innerHTML = `${imageDTO.creationTime}`;
                image.href = "#";
                image.innerHTML = `Ver imagen`;
                image.addEventListener("click", () => {
                    const newWindow = window.open();
                    newWindow.document.write(`
                        <html>
                        <head><title>Imagen ${count}</title></head>
                        <body>
                            <img src="data:image/png;base64, ${imageData}" alt="Imagen ${count}" />
                        </body>
                        </html>
                    `);
                    newWindow.document.close();
                });

                row.appendChild(imageIdTd);
                row.appendChild(creationDateTd);
                row.appendChild(creationTimeTd);
                row.appendChild(image);
            }
            count++;
            contentDiv.appendChild(row);
        });


    } catch (error) {
        console.error('Error al obtener la imagen:', error);
    }
}
