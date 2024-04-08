# Lógica por Trás do Back-End do Projeto

![Gráfico do Back-End](doc/back-end-aps.png)

O funcionamento do back-end do projeto segue uma lógica estruturada em diversas etapas:

1. **Entrada no Servidor HTTP:** O fluxo inicial se inicia com a entrada no servidor HTTP.

2. **Roteamento para as Rotas Disponíveis:**
  - `/`: Rota principal.
  - `/submit`: Rota de envio de dados.

3. **Controller:** Após entrar em uma das rotas possíveis, o controller é acionado para tratar a requisição.

4. **Model:** O controller encaminha a requisição para o model, informando o valor a ser inserido, buscado ou selecionado no banco de dados.

5. **Banco de Dados:** O model interage com o banco de dados para executar a operação requisitada.

6. **DTO (Data Transfer Object):** Após a operação no banco de dados, o resultado é enviado para o DTO.

7. **Handler HTTP:** O DTO é então repassado para o handler HTTP, que é responsável por gerenciar a comunicação entre o back-end e o cliente.

8. **Service:** O handler utiliza um serviço para processar os dados recebidos e retornar a resposta para o cliente.

## Exemplo de Configuração do Arquivo .env para o Projeto Funcionar com Dois Containers

```plaintext
MYSQL_URL=jdbc:mysql://mysql:3306/aps
MYSQL_USER=root
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=aps
```

## Como Fazer o Upload de uma Imagem para o Docker Hub

1. **Construa a Imagem com Tag:**
   ```bash
   docker build -t nome_do_seu_usuario/nome_do_repositorio:tag_atual .
   ```

2. **Re-tag da Imagem:**
   ```bash
   docker tag nome_do_seu_usuario/nome_do_repositorio:tag_atual nome_do_seu_usuario/nome_do_repositorio:tag_atualizada
   ```

3. **Faça o Push da Imagem:**
   ```bash
   docker push nome_do_seu_usuario/nome_do_repositorio:tag_atualizada
   ```

Essas etapas garantem que sua imagem seja construída, marcada corretamente e enviada para o Docker Hub.