<script>
   import { GlobalReferences } from "$lib/globals";
   import { onMount } from "svelte";

   let globals = new GlobalReferences();

   let selectedStudySet = {flashcard: {term:"test", definition: "testing"}, connections: [{term: ""}, {term: ""}, {term: ""}]}
   let currentFlashcardSide = selectedStudySet.flashcard.term;
   let studyPageDisplay = "none";
   let studySelectionPageDisplay = "flex";
   let userStudySets = [];
   let userStudySetSelection = [];
   let sharedStudySets = [];
   let sharedStudySetSelection = [];

   onMount(async () => {
      // get all of the users flashcardSets
      const responseToGetAll = await fetch(globals.backendBasePath + "/FlashcardSet/getAll", {
         mode: "same-origin",
         method: "get",
         credentials: "include"
      });

      if (responseToGetAll.ok) {
         userStudySets = await responseToGetAll.json();
      }
   });

   function startStudySession() {
      studySelectionPageDisplay = "none";
      studyPageDisplay = "flex";
   }
</script>

<div id="study-selection" style="display: {studySelectionPageDisplay};">
   <div id="user-study-sets">
      <h1>My Study Sets</h1>
      <div class="study-sets">
      {#each userStudySets as userStudySet}
         <div class="selections">
            <input type="checkbox" id="{userStudySet.setID}" name="{userStudySet.setID}" bind:group={userStudySetSelection} value="{userStudySet.setID}">
            <label for="{userStudySet.setID}">{userStudySet.setName}</label>
         </div>
      {/each}
      </div>
   </div>
   <div id="friends-study-sets">
      <h1>Shared Study Sets</h1>
      <div class="study-sets">
      {#each sharedStudySets as sharedStudySet}
         <div class="selections">
            <input type="checkbox" id="{sharedStudySet.setID}" name="{sharedStudySet.setID}" bind:group={sharedStudySetSelection} value="{sharedStudySet.setID}">
            <label for="{sharedStudySet.setID}">{sharedStudySet.setName}</label>
         </div>
      {/each}
      </div>
   </div>
   <button id="start-study-ses-button" on:click={startStudySession}>Start Study Session</button>
</div>
<div id="study-page" style="display: {studyPageDisplay};">
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

   #start-study-ses-button {
      margin-top: 48px;
      width: 352px;
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

   #study-page, #study-selection {
      display: flex;
      flex-direction: column;
      align-items: center;
   }

   #flashcard-nav {
      margin-top: 24px;
   }

   #user-study-sets {
      display: grid;
      align-items: center;
      justify-content: center;
      width: 352px;
   }

   h1 {
      text-align: center;
   }

   .study-sets {
      overflow: auto;
      width: 352px;
      height: 240px;
      background-color: #F5F5F5;
      box-sizing: border-box;
      font-size: 24px;
      border: 1px solid black;
      box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
   }

   label {
      display: inline; 
   }

   .selections {
      height: 30px;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
   }

   input[type="checkbox"] {
      transform: scale(1.25);
      accent-color: var(--primary-bg-color);
   }
</style>