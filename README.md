# Sobre o projeto

> O projeto Journey tem como objetivo ajudar o usuário a organizar viagens à trabalho ou lazer. O usuário pode criar
> uma viagem com nome, data de início e fim. Dentro da viagem o usuário pode planejar sua viagem adicionando atividades
> para realizar em cada dia.

## Requisitos

### Requisitos funcionais

1. O usuário cadastra uma viagem informando o local de destino, 
data de início, data de término, e-mails dos convidados e também seu nome completo e endereço de e-mail;
2. O criador da viagem recebe um e-mail para confirmar a nova viagem através de um link. Ao clicar no link,
a viagem é confirmada, os convidados recebem e-mails de confirmação de presença e o criador é redirecionado para a página da viagem;
3. Os convidados, ao clicarem no link de confirmação de presença, são redirecionados para a aplicação onde devem inserir seu nome (além do e-mail que já estará preenchido) e então estarão confirmados na viagem;
4. Na página do evento, os participantes da viagem podem adicionar links importantes da viagem como reserva do AirBnB, locais para serem visitados, etc...
5. Ainda na página do evento, o criador e os convidados podem adicionar atividades que irão ocorrer durante a viagem com título, data e horário;
6. Novos participantes podem ser convidados dentro da página do evento através do e-mail e assim devem passar pelo fluxo de confirmação como qualquer outro convidado


## Documentação
- Endpoint de cadastro de viagem `POST **/trips`
- Endpoint de consulta de viagem `GET**/trips/{tripId}`
- Endpoint de actualização de viagem `PUT**/trips/{tripId}`
- Endpoint confirmação de viagem `GET**/trips/{tripId}/confirm`
- Endpoint confirmação de participante `POST**/participants/{participantId}/confirm`
- Endpoint para convidar participante `POST**/trips/{tripId}/invites`
- Endpoint para consultar participantes `GET**/trips/{tripId}/participants`
- Endpoint para criação de link `POST**/trips/{tripId}/links`
- Endpoint para consultar links de uma viagem `GET**/trips/{tripId}/links`
- Endpoint para cadastro de atividade `POST**/trips/{tripId}/activities`
- Endpoint para consultar atividades de uma viagem `GET**/trips/{tripId}/invites`


## Features extras
- [ ] Criar um Service no pacote "trips" que será responsável por gerir as regras de negócio das 
    viagens e interagir com a base de dados
- [ ]  Adicionar uma validação nos campos de data
   - [ ]  A data de começo da viagem, é inferior a data de término viagem
   - [ ]  A data de uma atividade está entre as datas da viagem
        
       **Exemplo:**
       Viagem entre os dias 20 á 25 de julho no Rio de Janeiro
        
       ⇒ Atividade 19 de julho
        
       ⇒ Atividade 21 de julho
        
- [ ]  Extração do core das trips pra dentro de uma classe Service
- [ ]  Mapeamento das exceções da nossa aplicação
    - [ ]  Com tratativas de erro personalizadas