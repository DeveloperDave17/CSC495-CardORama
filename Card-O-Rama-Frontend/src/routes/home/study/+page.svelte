<script>
   let selectedStudySet = {flashcard: {term:"test", definition: "testing"}, connections: [{term: ""}, {term: ""}, {term: ""}]}
   let currentFlashcardSide = selectedStudySet.flashcard.term;
</script>

<div id="study-page">
   <div id="flashcard-nav">
      <button id="prev-button">Prev</button>
      <button>Next</button>
   </div>
   <div id="selected-flashcard"><p>{currentFlashcardSide}</p></div>
   <button id="gen-connect-button">Generate Connectivity Graph</button>
   <div id="connections">
      <p>Suggested Connections</p>
      {#each selectedStudySet.connections as connection}
         <div class="connection"><p>{connection.term}</p></div>
      {/each}
   </div>
</div>

<style>
   @import "/static/styles/main.css";

   #connections {
      font-size: 24px;
   }

   .connection {
      width: 245px;
      height: 59px;
      border: 1px solid black;
      font-size: 24px;
      margin-bottom: 24px;
      box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
      display: grid;
      align-items: center;
      justify-content: center;
      overflow: auto;
   }

   .connection p {
      padding: 4px;
      margin: 0;
   }

   #selected-flashcard {
      width: 352px;
      height: 188px;
      box-sizing: border-box;
      font-size: 36px;
      border: 1px solid black;
      box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
      margin-bottom: 50px;
      margin-top: 12px;
      display: grid;
      align-items: center;
      justify-content: center;
      overflow: auto;
   }

   #selected-flashcard p {
      padding: 8px;
      margin: 0;
   }

   #flashcard-nav button {
      width: 123px;
      height: 57px;
      font-size: 24px;
      color: white;
      background-color: var(--primary-bg-color);
   }

   #prev-button {
      margin-right: 105px;
   }

   #gen-connect-button {
      background-color: var(--primary-bg-color);
      color: white;
      height: 57px;
      font-size: 24px;
      width: 352px;
   }

   #study-page {
      display: flex;
      flex-direction: column;
      align-items: center;
   }

   #flashcard-nav {
      margin-top: 24px;
   }
</style>