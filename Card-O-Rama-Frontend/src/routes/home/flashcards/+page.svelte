<script>
   import { fetchGet } from "$lib/network-utils";
   import { GlobalReferences } from "$lib/globals";
   import { onMount } from "svelte";
   
   let globals = new GlobalReferences();
   let flashcardSets = [];
   let currentFlashcardSet = {setName : ""};
   let flashcardSetListDisplayStatus = "none";
   let flashcards = [];

   async function getFlashcardSets() {
      let flashcardSetsResponse = fetchGet(globals.backendBasePath + "/FlashcardSet/getAll");
      flashcardSets = await flashcardSetsResponse.json();
      currentFlashcardSet = flashcardSets[0];
   }

   function toggleFlashcardSetList() {
      if (flashcardSetListDisplayStatus === "none") {
         flashcardSetListDisplayStatus = "block";
      } else {
         flashcardSetListDisplayStatus = "none";
      }
   }

   async function createNewFlashcard() {
      await fetch(globals.backendBasePath + "/Flashcard/create", {
               method: "post",
               headers: {
                     "Content-Type" : "application/json",
               },
               credentials: "include",
               body: JSON.stringify({
                  setID: `${currentFlashcardSet.setID}`,
                  term: "",
                  definition: "",
                  position: `${flashcards.length}`
               })
            }).then((response) => {
               if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
               return response.json();
            }).then((data) => {
               flashcards.push(data);
               flashcards = flashcards;
            });
   }

   function createNewFlashcardSet() {

   }

   onMount(async () => {
      // get the flashcardSets
      const responseToGetAll = await fetch(globals.backendBasePath + "/FlashcardSet/getAll", {
         mode: "same-origin",
         method: "get",
         credentials: "include"
      });

      // load the first flashcard set or create one if one doesn't exist.
      if (responseToGetAll.ok) {
         flashcardSets = await responseToGetAll.json();
         if (flashcardSets.length === 0) {
            const responseToCreate = await fetch(globals.backendBasePath + "/FlashcardSet/create", {
               method: "post",
               headers: {
                     "Content-Type" : "application/json",
               },
               credentials: "include",
               body: `{
                  "setName": "Flashcard Set 1",
                  "privacy": "PRIVATE",
                  "color": "#FFFFFF",
                  "priority": "1"
               }`
            });

            if (responseToCreate.ok) {
               flashcardSet = await responseToCreate.json();
               flashcardSets.push([flashcardSet]);
            }
         } else {
            currentFlashcardSet = flashcardSets[0];
         }

         // get all of the flashcards from the flashcard set
         const getAllFlashCards = await fetch(globals.backendBasePath + `/Flashcard/getAll/${currentFlashcardSet.setID}`, {
            mode: "same-origin",
            method: "get",
            credentials: "include"
         }); 

         if (getAllFlashCards.ok) {
            flashcards = await getAllFlashCards.json();
         }
      } else {
         window.location.href = globalReferences.indexlocation;
      }

   });
</script>

<div id="flashcard-set-page">
   <button id="flashcard-set-button" on:click={toggleFlashcardSetList}>
      <span>{currentFlashcardSet.setName}</span>
      <span></span>
   </button>
   <ul id="flashcard-set-list" style="display: {flashcardSetListDisplayStatus};">
      {#each flashcardSets as flashcardSet}
         <li>{flashcardSet.setName}</li>
      {/each}
      <li id="new-flashcard-set">+ New Flashcard Set</li>
   </ul>

   <div class="flashcard-set-values">
      <label for="set-name">Flashcard Set Name</label>
      <input type="text" id="set-name" name="set-name" value={currentFlashcardSet.setName}>
   </div>
   <div class="flashcard-set-values">
      <label for="set-color">Flashcard Set Color</label>
      <select id="set-color" name="set-color" value={currentFlashcardSet.color}>
         <option value="#FFFFFF">White</option>
         <option value="#C7EEFF">Light Blue</option>
         <option value="#CFFFC7">Light Green</option>
         <option value="#FEFFC0">Yellow</option>
         <option value="#FFC5C5">Light Red</option>
         <option value="#FFC5F6">Purple</option>
      </select>
   </div>
   <div class="flashcard-set-values">
      <label for="set-privacy">Flashcard Set Privacy</label>
      <select id="set-privacy" name="set-privacy" value={currentFlashcardSet.privacy}>
         <option value="PRIVATE">Private</option>
         <option value="FRIENDS">Friends-Only</option>
         <option value="PUBLIC">Public</option>
      </select> 
   </div>

   <h1>Flashcards</h1>
   {#each flashcards as flashcard}
      <div class="flashcard">
         <label for="term{flashcard.flashcardID}">Term</label>
         <input class="term-inputs" type="text" id="term{flashcard.flashcardID}" name="term{flashcard.flashcardID}" value={flashcard.term}>
         <label for="definition{flashcard.flashcardID}">Definition</label>
         <textarea class="definitions" id="definition{flashcard.flashcardID}" name="definition{flashcard.flashcardID}" value={flashcard.definition} rows="4"/>
      </div>
   {/each}
   <button id="new-flashcard-button" on:click={createNewFlashcard}>Create New Flashcard</button>
</div>

<style>
   @import "/static/styles/main.css";

   #flashcard-set-page {
      display: flex;
      flex-direction: column;
      align-items: center;
   }

   #flashcard-set-button {
      height: 115px;
      width: 332px;
      font-size: 24px;
      margin-top: 50px;
      margin-bottom: 20px;
      border-radius: 10px;
      background-color: #F5F5F5;
      border-color: #BBBBBB;
      border-width: 1px;
      font-weight: 500;
   }

   #flashcard-set-list {
      width: 332px;
      height: 216px;
      font-size: 20px;
      background-color: #F5F5F5;
      border-color: #BBBBBB;
      border-radius: 10px;
      border-style: solid;
      border-width: 1px;
      padding-left: 0px;
   }

   #flashcard-set-list > li {
      list-style-type: none;
      padding: 0 15px 0 15px;
      margin: 0px;
      height: 40px;
      display: flex;
      align-items: center;
   }

   .flashcard-set-values {
      display: flex;
      flex-direction: column;
      width: 332px;
      margin-bottom: 24px;
   }

   .flashcard-set-values > label {
      font-size: 20px;
      font-weight: 500;
   }

   .flashcard {
      display: flex;
      box-sizing: border-box;
      width: 332px;
      flex-direction: column;
      padding: 20px;
      font-size: 20px;
      border-style: solid;
      border-color: black;
      border-radius: 10px;
      border-width: 1px;
      margin-bottom: 20px;
   }

   .term-inputs, .definitions {
      font-size: 20px;
      background-color: #F5F5F5;
      border-radius: 10px;
      border-color: #BBBBBB;
   }

   .term-inputs {
      margin-bottom: 20px; 
   }

   .flashcard-set-values > input, .flashcard-set-values > select, .flashcard-set-values > select > option {
      background-color: #F5F5F5;
      font-size: 20px;
      border-color: #BBBBBB;
      border-radius: 10px;
      border-style: solid;
   }

   #new-flashcard-button {
      width: 332px;
      height: 64px;
      background-color: var(--primary-bg-color);
      color: white;
      font-size: 20px;
      border-radius: 10px;
   }
</style>