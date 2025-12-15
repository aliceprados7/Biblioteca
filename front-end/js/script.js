const API = 'http://localhost:4567';

async function criarCliente() {

  const nome = document.getElementById("clienteNome").value
  const cpf = document.getElementById("clienteCpf").value;
  const telefone = document.getElementById("clienteTelefone").value
  
  novoCliente = {
    nome: nome,
    cpf:cpf,
    telefone:telefone
  }
  alert(novoCliente)
  console.log (novoCliente)

  const response = await fetch(`${API}/clientes`, {
    method:'POST',
    headers:{'Content-Type':'application/json'},
    body: JSON.stringify(novoCliente)
  });
  if(response.ok){
    document.getElementById("sucess").innerHTML = "Cliente Cadastrado com sucesso"

    document.getElementById("clienteNome").innerHTML = ""
    document.getElementById("clienteCpf").innerHTML = "";
    document.getElementById("clienteTelefone").innerHTML = ""
  } else{
    document.getElementById("sucess").innerHTML = "Erro de conexão"
  }
  listarClientes();
}

// ========================================================================================================================

async function criarLivro() {

  const nome = document.getElementById("livroNome").value
  const autor = document.getElementById("livroAutor").value;
  
  novoLivro = {
    nome: nome,
    autor:autor
  }
  console.log (novoLivro)

  const response = await fetch(`${API}/livros`, {
    method:'POST',
    headers:{'Content-Type':'application/json'},
    body: JSON.stringify(novoLivro)
  });
  if(response.ok){
    document.getElementById("sucessLivro").innerHTML = "Cliente Cadastrado com sucesso"

    document.getElementById("livroNome").innerHTML = ""
    document.getElementById("livroAutor").innerHTML = "";
  } else{
    document.getElementById("sucess").innerHTML = "Erro de conexão"
  }
  listarLivros();
}

//=======================================================================================================================================

async function criarEmprestimo() {
    const idCliente = parseInt(document.getElementById('empCliente').value);
    const idLivro = parseInt(document.getElementById('empLivro').value);
//   const idCliente = document.getElementById("empCliente").value;
//   const idLivro = document.getElementById("empLivro").value;
  const data1 = document.getElementById("data1").value;
  const data2 = document.getElementById("data2").value;

  novoEmprestimo = {
    id_cliente: parseInt(idCliente),
    id_livro: parseInt(idLivro),
    data_devolucao: data2
  }
  console.log(novoEmprestimo)

  const response = await fetch(`${API}/emprestimos`, {
    method:'POST',
    headers:{'Content-Type':'application/json'},
    body: JSON.stringify(novoEmprestimo)
  });

  if(response.ok){
    alert("sucesso")
  }
  
  listarEmprestimos();
}

//=====================================================================================================================================


async function listarClientes() {
  const res = await fetch(`${API}/clientes`);
  const data = await res.json();
  console.log(data)
  listaClientes.innerHTML = data.map(c => `<li>${c.id} - ${c.nome} - ${c.cpf} - ${c.telefone}</li>`).join('');
}

async function listarLivros() {
  const res = await fetch(`${API}/livros`);
  const data = await res.json();
  listaLivros.innerHTML = data.map(l => `<li>${l.id} - ${l.nome}</li>`).join('');
}

async function listarEmprestimos() {
  const res = await fetch(`${API}/emprestimos`);
  const data = await res.json();
  listaEmprestimos.innerHTML = data.map(e => `
    <li>ID ${e.id} | Cliente ${e.id_cliente} | Livro ${e.id_livro} |</li>
  `).join('');
}   

// const API = "http://localhost:4567";

// // CLIENTE
// function criarCliente() {
//     fetch(`${API}/clientes`, {
//         method: "POST",
//         mode: "no-cors",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify({
//             nome: clienteNome.value
//         })
//     }).then(() => listarClientes());
// }

// function listarClientes() {
//     fetch(`${API}/clientes`)
//         .then(res => res.json())
//         .then(clientes => {
//             listaClientes.innerHTML = "";
//             clientes.forEach(c => {
//                 listaClientes.innerHTML += `
//                     <li>
//                         ID ${c.id} - ${c.nome}
//                         <button onclick="editarCliente(${c.id}, '${c.nome}')">Editar</button>
//                         <button onclick="excluirCliente(${c.id})">Excluir</button>
//                     </li>`;
//             });
//         });
// }

// function editarCliente(id, nomeAtual) {
//     const novoNome = prompt("Digite o novo nome:", nomeAtual);
//     if (novoNome && novoNome !== nomeAtual) {
//         fetch(`${API}/clientes/${id}`, {
//             method: "PUT",
//             headers: { "Content-Type": "application/json" },
//             body: JSON.stringify({ nome: novoNome })
//         }).then(() => listarClientes());
//     }
// }

// function excluirCliente(id) {
//     if (confirm(`Tem certeza que deseja excluir o cliente ID ${id}?`)) {
//         fetch(`${API}/clientes/${id}`, {
//             method: "DELETE"
//         }).then(() => listarClientes());
//     }
// }

// // LIVRO
// function criarLivro() {
//     fetch(`${API}/livros`, {
//         method: "POST",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify({
//             nome: livroNome.value,
//             autor: livroAutor.value
//         })
//     }).then(() => listarLivros());
// }

// function listarLivros() {
//     fetch(`${API}/livros`)
//         .then(res => res.json())
//         .then(livros => {
//             listaLivros.innerHTML = "";
//             livros.forEach(l => {
//                 listaLivros.innerHTML += `<li>ID ${l.id} - ${l.nome}</li>`;
//             });
//         });
// }

// // EMPRESTIMO
// function criarEmprestimo() {
//     fetch(`${API}/emprestimos`, {
//         method: "POST",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify({
//             idCliente: Number(empClienteId.value),
//             idLivro: Number(empLivroId.value)
//         })
//     }).then(() => listarEmprestimos());
// }

// function listarEmprestimos() {
//     fetch(`${API}/emprestimos`)
//         .then(res => res.json())
//         .then(emps => {
//             listaEmprestimos.innerHTML = "";
//             emps.forEach(e => {
//                 listaEmprestimos.innerHTML +=
//                     `<li>ID ${e.id} | Cliente ${e.idCliente} | Livro ${e.idLivro}</li>`;
//             });
//         });
// }
