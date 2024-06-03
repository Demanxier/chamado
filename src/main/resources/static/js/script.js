// Função para cadastrar um desenvolvedor
function cadastrarDev() {
    var nome = document.getElementById("nome").value;
    var email = document.getElementById("email").value;
    var custo = document.getElementById("custo").value;
    var consultor = document.getElementById("consultor").checked;

    var dados = {
        nome: nome,
        email: email,
        custo: custo,
        consultor: consultor
    };

    fetch('https://demanxier-chamados.up.railway.app/api/v1/dev', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dados)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao cadastrar desenvolvedor');
        }
        return response.json();
    })
    .then(data => {
        document.getElementById("cadastroDevForm").reset();
        alert('Desenvolvedor cadastrado com sucesso!');
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao cadastrar desenvolvedor');
    });
}

// Função para verificar se o chamado existe
function verificarChamadoExistente(idChamado) {
    return fetch(`https://demanxier-chamados.up.railway.app/api/v1/chamado/${idChamado}`)
    .then(response => {
        if (!response.ok) {
            throw new Error('Chamado não encontrado');
        }
        return true;
    })
    .catch(error => {
        console.error('Erro:', error);
        return false;
    });
}

// Função para cadastrar um atendimento
function CadastrarAtendimento() {
    var idChamado = document.getElementById("idChamado").value;
    var descricao = document.getElementById("descricao").value;
    var horaInicio = document.getElementById("horaInicio").value;
    var horaFim = document.getElementById("horaFim").value;

    // Verifica se o chamado existe
    verificarChamadoExistente(idChamado)
    .then(chamadoExiste => {
        if (chamadoExiste) {
            var dados = {
                data: new Date().toISOString().split('T')[0], // Adiciona a data atual no formato 'YYYY-MM-DD'
                descricao: descricao,
                horaInicio: horaInicio,
                horaFim: horaFim,
                chamado: {
                    id: parseInt(idChamado)
                }
            };

            console.log('Dados enviados:', JSON.stringify(dados));

            return fetch('https://demanxier-chamados.up.railway.app/api/v1/atendimento', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dados)
            });
        } else {
            throw new Error('ID do chamado não existe');
        }
    })
    .then(response => {
        console.log('Resposta do servidor:', response);

        if (!response.ok) {
            return response.text().then(text => { throw new Error('Erro ao realizar atendimento: ' + text); });
        }
        
        return response.text(); // Modificação para lidar com resposta de texto
    })
    .then(data => {
        document.getElementById("cadastroAtendimento").reset();
        alert('Atendimento realizado com sucesso!');
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao realizar atendimento: ' + error.message);
    });
}

//Função para listar Atendimentos
document.getElementById('filtroPeriodo').addEventListener('submit', function(event) {
    event.preventDefault();
    var dataInicio = document.getElementById('dataInicio').value;
    var dataFim = document.getElementById('dataFim').value;

    fetch(`https://demanxier-chamados.up.railway.app/api/v1/atendimento?dataInicio=${dataInicio}&dataFim=${dataFim}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar atendimentos');
            }
            return response.json();
        })
        .then(atendimentos => {
            var tabelaAtendimentos = document.getElementById('tabelaAtendimentos').getElementsByTagName('tbody')[0];
            tabelaAtendimentos.innerHTML = '';
            atendimentos.forEach(atendimento => {
                var row = tabelaAtendimentos.insertRow();
                row.insertCell(0).innerText = atendimento.id;
                row.insertCell(1).innerText = atendimento.data;
                row.insertCell(2).innerText = atendimento.horaInicio;
                row.insertCell(3).innerText = atendimento.horaFim;
                row.insertCell(4).innerText = atendimento.descricao;
                row.insertCell(5).innerText = atendimento.custo; 
            });
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Erro ao buscar atendimentos: ' + error.message);
        });
});


//Função para atualizar chamados
function buscarChamado() {
    var idChamado = document.getElementById('idChamado').value;
    
    fetch(`https://demanxier-chamados.up.railway.app/api/v1/chamado/${idChamado}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Chamado não encontrado');
            }
            return response.json();
        })
        .then(chamado => {
            document.getElementById('descricao').value = chamado.descricao;
            document.getElementById('status').value = chamado.status;
            document.getElementById('chamadoDetalhes').style.display = 'block';
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Erro ao buscar chamado: ' + error.message);
        });
}

document.getElementById('atualizarChamado').addEventListener('submit', function(event) {
    event.preventDefault();
    
    var idChamado = document.getElementById('idChamado').value;
    var descricao = document.getElementById('descricao').value;
    var status = document.getElementById('status').value;

    var dados = {
        descricao: descricao,
        status: status
    };

    fetch(`https://demanxier-chamados.up.railway.app/api/v1/chamado/${idChamado}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dados)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error('Erro ao atualizar chamado: ' + text); });
        }
        alert('Chamado atualizado com sucesso!');
        document.getElementById('atualizarChamado').reset();
        document.getElementById('chamadoDetalhes').style.display = 'none';
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao atualizar chamado: ' + error.message);
    });
});
