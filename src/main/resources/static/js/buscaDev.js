// Função para buscar os desenvolvedores na API e preencher o campo de seleção
function buscarDesenvolvedores() {
    fetch('http://localhost:8080/api/v1/dev')
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao buscar desenvolvedores');
        }
        return response.json();
    })
    .then(data => {
        var selectDesenvolvedor = document.getElementById("desenvolvedor");
        // Limpa as opções existentes
        selectDesenvolvedor.innerHTML = "";
        // Adiciona uma opção padrão
        var defaultOption = document.createElement("option");
        defaultOption.text = "Selecione um desenvolvedor";
        selectDesenvolvedor.appendChild(defaultOption);
        // Adiciona as opções dos desenvolvedores
        data.forEach(dev => {
            var option = document.createElement("option");
            option.value = dev.id;
            option.text = dev.nome;
            selectDesenvolvedor.appendChild(option);
        });
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao buscar desenvolvedores');
    });
}

// Chama a função buscarDesenvolvedores() quando a página terminar de carregar
window.addEventListener('load', buscarDesenvolvedores);

   
// Função para cadastrar um chamado
function CadastrarChamado() {
    var numeroTicket = document.getElementById("numeroTicket").value;
    var responsavel = document.getElementById("responsavel").value;
    var resumo = document.getElementById("resumo").value;
    var status = document.getElementById("status").value;
    var descricao = document.getElementById("descricao").value;

    var desenvolvedorSelect = document.getElementById("desenvolvedor");
    var desenvolvedorSelecionado = desenvolvedorSelect.options[desenvolvedorSelect.selectedIndex];
    var desenvolvedorId = desenvolvedorSelecionado.value;
    var desenvolvedorNome = desenvolvedorSelecionado.text;

    var dados = {
        numeroTicket: numeroTicket,
        responsavel: responsavel,
        resumo: resumo,
        status: status,
        descricao: descricao,
        desenvolvedor: {
            id: desenvolvedorId,
            nome: desenvolvedorNome
        }
    };

    fetch('http://localhost:8080/api/v1/chamado', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dados)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao cadastrar chamado');
        }
        return response.json();
    })
    .then(data => {
        document.getElementById("cadastroChamadoForm").reset();
        alert('Chamado cadastrado com sucesso!');
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao cadastrar chamado');
    });
}
