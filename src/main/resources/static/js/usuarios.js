document.getElementById("crear-empleado").addEventListener("click", async function(event) {
    event.preventDefault();
    document.getElementById("content").innerHTML = await traerContenido("usuarios");
    asignarEventos();
});

let asignarEventos = () => {
    document.getElementById("save").addEventListener("click", async function(event) {
        event.preventDefault();
        await guardarUsuario();
        window.location.reload();
    });
}

let guardarUsuario = async () => {

    let id = document.getElementById("id").value;
    let name = document.getElementById("name").value;
    let lastName = document.getElementById("last_name").value;
    let email = document.getElementById("email").value;
    let birthdate = document.getElementById("birthdate").value;
    let direction = document.getElementById("direction").value;
    let phone = document.getElementById("phone").value;

    let usuario = {
        id: id,
        name: name,
        lastName: lastName,
        email: email,
        birthdate: birthdate,
        direction: direction,
        phone: phone
    }

    let response = await fetch(`/employees`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuario)
    });

    if (!response.ok) {
        throw new Error('Error al guardar el usuario');
    }

}