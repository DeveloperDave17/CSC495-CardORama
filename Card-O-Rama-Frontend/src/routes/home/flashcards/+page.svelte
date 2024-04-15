<script>
   import { fetchGet } from "$lib/network-utils";
   import { GlobalReferences } from "$lib/globals";
   import { onMount } from "svelte";
   import { overrideItemIdKeyNameBeforeInitialisingDndZones } from "svelte-dnd-action";
   import { dndzone } from "svelte-dnd-action";
   overrideItemIdKeyNameBeforeInitialisingDndZones("flashcardID");
   
   let globals = new GlobalReferences();
   let flashcardSets = [];
   let currentFlashcardSet = {setName : "", color : "FFFFFF", privacy : "FRIENDS"};
   let flashcardSetListDisplayStatus = "none";
   let flashcards = [];
   $: flashcardSetLength = flashcardSets.length;
   $: flashcardColors = "#" + currentFlashcardSet.color;

   function getFlashcards(flashcardSetID) {
      fetch(globals.backendBasePath + `/Flashcard/getAll/${flashcardSetID}`, {
            mode: "same-origin",
            method: "get",
            credentials: "include"
         }).then((response) => {
            if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
            return response.json();
         }).then((data) => {
            flashcards = data;
         });
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
      fetch(globals.backendBasePath + "/FlashcardSet/create", {
               method: "post",
               headers: {
                     "Content-Type" : "application/json",
               },
               credentials: "include",
               body: JSON.stringify({
                  setName: `Flashcard Set ${flashcardSetLength}`,
                  privacy: "PRIVATE",
                  color: "#FFFFFF",
                  priority: `${flashcardSetLength}`
               })
            }).then((response) => {
               if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
               return response.json();
            }).then((data) => {
               flashcardSets.push(data);
               flashcardSets = flashcardSets;
            });
   }

   onMount(async () => {
      try {
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
      } catch (exception)  {
         window.location.href = globalReferences.indexlocation;
      }
   });

   function changeFlashcardSet(flashcardSet) {
      currentFlashcardSet = flashcardSet;
      getFlashcards(flashcardSet.setID);
   }

   function updateFlashcardTerm(flashcard) {
      let flashcardTerm = flashcard.term;
      let encodedFlashcardTerm = encodeURIComponent(flashcardTerm);
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/Flashcard/updateTerm/${flashcard.flashcardID}/${encodedFlashcardTerm}`, {
               method: "post",
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
            });
   }

   function updateFlashcardDefinition(flashcard) {
      let setDefinition = flashcard.definition;
      let encodedDefintion = encodeURIComponent(setDefinition);
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/Flashcard/updateDefinition/${flashcard.flashcardID}/${encodedDefintion}`, {
               method: "post",
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
            });
   }

   function updateFlashcardSetName(flashcardSet) {
      let setName = flashcardSet.setName;
      let encodedName = encodeURIComponent(setName);
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/FlashcardSet/updateName/${flashcardSet.setID}/${encodedName}`, {
               method: "post",
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
            });
      // Ensures the flashcard sets update for the user.
      flashcardSets = flashcardSets;
   }

   function updateFlashcardSetColor(flashcardSet) {
      let globals = new GlobalReferences();
      let path = `/FlashcardSet/updateColor/${flashcardSet.setID}/${flashcardSet.color}`; 
      fetch(globals.backendBasePath + path, {
               method: "post", 
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
            });
   }

   function updateFlashcardSetPrivacy(flashcardSet) {
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/FlashcardSet/updatePrivacy/${flashcardSet.setID}/${flashcardSet.privacy}`, {
               method: "post",
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
            });
   }

   function removeFlashcard(flashcard) {
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/Flashcard/remove/${flashcard.flashcardID}`, {
               method: "post",
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
            });
      let index = flashcards.indexOf(flashcard);
      flashcards.splice(index, 1);
      // update flascards for the user
      flashcards = flashcards;
   }

   function removeFlashcardSet(flashcardSet) {
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/FlashcardSet/remove/${flashcardSet.setID}`, {
               method: "post",
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
            });
      let index = flashcardSets.indexOf(flashcardSet);
      flashcardSets.splice(index, 1);
      flashcardSets = flashcardSets;
   } 

   const handleFlashcardConsider = (event) => {
      flashcards = event.detail.items;
   }

   const handleFlashcardFinalize = (event) => {
      flashcards = event.detail.items;
      sendNewPositions();
   }

   function sendNewPositions() {
      let positions = flashcards.map((flashcard) => flashcard.flashcardID);
      // send new positions
      fetch(globals.backendBasePath + `/Flashcard/updatePositions`, {
               method: "post",
               headers: {
                     "Content-Type" : "application/json",
               },
               credentials: "include",
               body: JSON.stringify(positions)
            }).then((response) => {
               if (!response.ok) {
                  window.location.href = globalReferences.indexlocation;
               }
            });
   }
</script>

<div id="flashcard-set-page">
   <button id="flashcard-set-button" on:click={toggleFlashcardSetList}>
      <span>{currentFlashcardSet.setName}</span>
   </button>
   <ul id="flashcard-set-list" style="display: {flashcardSetListDisplayStatus};">
      {#each flashcardSets as flashcardSet}
         <div class="flashcard-set-list-ele">
            <li on:click={() => changeFlashcardSet(flashcardSet)}>{flashcardSet.setName}</li>
            {#if flashcardSet != currentFlashcardSet}
               <img class="flashcardset-trashcan" src="/images/trash-can-outline.svg" alt="delete flashcard set button" on:click={() => {removeFlashcardSet(flashcardSet)}}>
            {/if}
         </div>
      {/each}
      <li id="new-flashcard-set" on:click={createNewFlashcardSet}>+ New Flashcard Set</li>
   </ul>

   <div class="flashcard-set-values">
      <label for="set-name">Flashcard Set Name</label>
      <input type="text" id="set-name" name="set-name" bind:value={currentFlashcardSet.setName} on:change={() => updateFlashcardSetName(currentFlashcardSet)}>
   </div>
   <div class="flashcard-set-values">
      <label for="set-color">Flashcard Set Color</label>
      <select id="set-color" name="set-color" bind:value={currentFlashcardSet.color} on:change={() => updateFlashcardSetColor(currentFlashcardSet)}>
         <option value="FFFFFF">White</option>
         <option value="C7EEFF">Light Blue</option>
         <option value="CFFFC7">Light Green</option>
         <option value="FEFFC0">Yellow</option>
         <option value="FFC5C5">Light Red</option>
         <option value="FFC5F6">Purple</option>
      </select>
   </div>
   <div class="flashcard-set-values">
      <label for="set-privacy">Flashcard Set Privacy</label>
      <select id="set-privacy" name="set-privacy" bind:value={currentFlashcardSet.privacy} on:change={() => updateFlashcardSetPrivacy(currentFlashcardSet)}>
         <option value="PRIVATE">Private</option>
         <option value="FRIENDS">Friends-Only</option>
         <option value="PUBLIC">Public</option>
      </select> 
   </div>

   <h1 id="flashcards-title">Flashcards</h1>
   <div class="flashcard-container">
      <section
         use:dndzone="{{ items: flashcards, dropTargetStyle: {} }}"
         on:consider="{handleFlashcardConsider}"
         on:finalize="{handleFlashcardFinalize}"
      >
      {#each flashcards as flashcard(flashcard.flashcardID)}
         <div class="flashcard" style="background-color: {flashcardColors};">
            <div class="flashcard-top">
               <label for="term{flashcard.flashcardID}">Term</label>
               <img class="flashcard-trashcan" src="/images/trash-can-outline.svg" alt="delete button" on:click={() => removeFlashcard(flashcard)}>
            </div>
            <textarea class="term-inputs" type="text" id="term{flashcard.flashcardID}" name="term{flashcard.flashcardID}" bind:value={flashcard.term} on:change={() => updateFlashcardTerm(flashcard)} rows="1"/>
            <label for="definition{flashcard.flashcardID}">Definition</label>
            <textarea class="definitions" id="definition{flashcard.flashcardID}" name="definition{flashcard.flashcardID}" bind:value={flashcard.definition} on:change={() => updateFlashcardDefinition(flashcard)} rows="4"/>
         </div>
      {/each}
      </section>
   </div>
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
      overflow: clip;
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
      overflow-y: scroll;
      overflow-x: ellipsis;
   }

   #flashcard-set-list::-webkit-scrollbar {
      width: 16px;
      border-radius: 10px;
   }

   .term-inputs::-webkit-scrollbar, .definitions::-webkit-scrollbar {
      width: 8px;
      border-radius: 10px;
   }

   #flashcard-set-list::-webkit-scrollbar-track, .term-inputs::-webkit-scrollbar-track, .definitions::-webkit-scrollbar-track {
      box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
      border-radius: 10px;
   }

   #flashcard-set-list::-webkit-scrollbar-thumb, .term-inputs::-webkit-scrollbar-thumb, .definitions::-webkit-scrollbar-thumb {
      background-color: var(--primary-bg-color);
      border-radius: 10px;
      outline: 1px solid black;
   }

   #flashcard-set-list > li, .flashcard-set-list-ele {
      list-style-type: none;
      padding: 0 15px 0 15px;
      margin: 0px;
      height: 40px;
      display: flex;
      align-items: center;
   }

   .flashcard-set-list-ele {
      display: flex;
      justify-content: space-between;
   }

   .flashcard-set-list-ele > li {
      text-overflow: ellipsis;
      overflow: clip;
      height: 18px;
      width: 250px;
   }

   .flashcardset-trashcan {
      width: 25px;
      height: 25px;
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

   .flashcard-trashcan {
      height: 25px;
      width: 25px;
   }

   .flashcard-top {
      display: flex;
      justify-content: space-between;
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
      margin-bottom: 32px;
   }

   :global(body) {
      overflow-y: scroll;
   }

   @media screen and (min-width: 769px) {
      .flashcard, #flashcard-set-button, #flashcard-set-list, .flashcard-set-values, #new-flashcard-button {
         width: clamp(600px, 70vw, 800px);
      }

      #flashcard-set-button {
         font-size: 32px;
      }

      #flashcard-set-list {
         font-size: 24px;
      }

      .flashcard-set-list-ele > li {
         height: 24px;
      }

      .flashcard-set-values > label, .flashcard-set-values > input, .flashcard-set-values > select {
         font-size: 28px;
      }

      #flashcard-set-title {
         font-size: 32px;
      }

      .flashcard, .term-inputs, .definitions {
         font-size: 24px;
      }
   }
</style>