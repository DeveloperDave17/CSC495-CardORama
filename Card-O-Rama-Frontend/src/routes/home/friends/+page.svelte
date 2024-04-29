<script>
	import { goto } from "$app/navigation";
   import { GlobalReferences } from "$lib/globals";
	import { onMount } from "svelte";
   import Swal from "sweetalert2";

   // friend display logic
   let friends = [];
   let friendFilter = "";
   $: filteredFriends = friends.filter((friend) => friend.friendEmail.includes(friendFilter));

   let userFriendRequest = "";

   // pending friend requests display logic
   let friendRequests = [];
   let friendRequestFilter = "";
   $: filteredFriendRequests = friendRequests.filter((friendRequest) => friendRequest.email.includes(friendRequestFilter));

   let friendsListDisplay = "flex";
   let flashcardSetsDisplay = "none";
   let flashcardsDisplay = "none";

   let flashcardSetsAndFavorites = [];
   let flashcards = [];
   let flashcardColors;

   let innerWidth;
   let desktopMinWidth = 769;
   let desktopDefRowCount = 3;
   let mobileDefRowCount = 4;
   $: definitionRowCount = innerWidth > desktopMinWidth ? desktopDefRowCount : mobileDefRowCount;

   function sendFriendRequest() {
      let encodedEmail = encodeURIComponent(userFriendRequest);
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/Friend/sendFriendRequest/${encodedEmail}`, {
               method: "post",
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  goto("/");
               }
               Swal.fire({
                     title: "Friend Request Successfully Sent!",
                     icon: "success"
                  });
            });
      userFriendRequest = "";
      // incase a friendrequest is removed or a friend is added.
      reloadLists();
   }

   function removeFriendship(friend) {
      let encodedEmail = encodeURIComponent(friend.friendEmail);
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/Friend/removeFriendship/${encodedEmail}`, {
               method: "post",
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  goto("/");
               }
               Swal.fire({
                     title: "Friend Successfully Removed.",
                     icon: "success"
                  });
               // reload the users friends list to reflect the change.
               reloadFriendsList();
            });
   }

   function reloadFriendsList() {
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + "/Friend/getFriendsList", {
            mode: "same-origin",
            method: "get",
            credentials: "include"
         }).then((response) => {
            if (!response.ok) {
               goto("/");
            } else {
               return response.json();
            }
         }).then((data) => {
            friends = data;
         });
   }

   function reloadFriendRequestsList() {
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + "/Friend/getFriendRequestsList", {
            mode: "same-origin",
            method: "get",
            credentials: "include"
         }).then((response) => {
            if (!response.ok) {
               goto("/");
            } else {
               return response.json();
            }
         }).then((data) => {
            friendRequests = data;
         });
   }

   function reloadLists() {
      reloadFriendsList();
      reloadFriendRequestsList();
   }

   function rejectFriendRequest(friendRequest) {
      let encodedEmail = encodeURIComponent(friendRequest.email);
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/Friend/declineFriendRequest/${encodedEmail}`, {
               method: "post",
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  goto("/");
               }
               Swal.fire({
                     title: "Friend Request Rejected.",
                     icon: "success"
                  });
               // reload the users friend requests list to reflect the change.
               reloadFriendRequestsList();
            });
   }

   onMount(() => {
      reloadLists();
   });

   function acceptFriendRequest(friendRequest) {
      let encodedEmail = encodeURIComponent(friendRequest.email);
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/Friend/acceptFriendRequest/${encodedEmail}`, {
               method: "post",
               credentials: "include"
            }).then((response) => {
               if (!response.ok) {
                  goto("/");
               }
               Swal.fire({
                     title: "Friend Request Accepted.",
                     icon: "success"
                  });
               // reload the users friend requests list to reflect the change.
               reloadLists();
            });
   } 

   function openFriendFlashcardSets(friendEmail) {
      let encodedEmail = encodeURIComponent(friendEmail)
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/FlashcardSet/getAllSharedFlashcardSets/${encodedEmail}`, {
            mode: "same-origin",
            method: "get",
            credentials: "include"
         }).then((response) => {
            if (!response.ok) {
               goto("/");
            } else {
               return response.json();
            }
         }).then((data) => {
            flashcardSetsAndFavorites = data;
            flashcardSetsDisplay = "flex";
         });
   }

   function closeFriendFlashcardSets() {
      flashcardSetsDisplay = "none";
   }

   function viewFlashcards(flashcardSetID, flashcardSetColor) {
      flashcardColors = "#" + flashcardSetColor;
      let globals = new GlobalReferences();
      fetch(globals.backendBasePath + `/Flashcard/getAll/${flashcardSetID}`, {
            mode: "same-origin",
            method: "get",
            credentials: "include"
         }).then((response) => {
            if (!response.ok) {
               goto("/");
            } else {
               return response.json();
            }
         }).then((data) => {
            flashcards = data;
            flashcardSetsDisplay = "none";
            friendsListDisplay = "none";
            flashcardsDisplay= "flex";
         });
   }

   function closeFlashcards() {
      flashcardsDisplay = "none";
      flashcardSetsDisplay = "flex";
      friendsListDisplay = "flex";
   }

   function toggleFavorited(flashcardSetAndFavorite) {
      let globals = new GlobalReferences();
      if (!flashcardSetAndFavorite.favorited) {
         fetch(globals.backendBasePath + `/Favorite/add/${flashcardSetAndFavorite.flashcardSet.setID}`, {
                  method: "post",
                  credentials: "include"
               }).then((response) => {
                  if (!response.ok) {
                     goto("/");
                  }
                  flashcardSetAndFavorite.favorited = true;
                  flashcardSetsAndFavorites = flashcardSetsAndFavorites;
               });
      } else {
         fetch(globals.backendBasePath + `/Favorite/remove/${flashcardSetAndFavorite.flashcardSet.setID}`, {
                  method: "post",
                  credentials: "include"
               }).then((response) => {
                  if (!response.ok) {
                     goto("/");
                  }
                  flashcardSetAndFavorite.favorited = false;
                  flashcardSetsAndFavorites = flashcardSetsAndFavorites;
               });
      }
   }
</script>

<svelte:window bind:innerWidth />
<div id="friends-page" style="display: {friendsListDisplay};">
   <h1>Friends</h1>
   <input class="filter-inputs" type="text" bind:value={friendFilter}>
   <div class="list-containers">
      <ul>
         {#each filteredFriends as friend}
            <div class="list-element">
               <li on:click={() => {openFriendFlashcardSets(friend.friendEmail)}}>{friend.friendEmail}</li>
               <img src="/images/trash-can-outline.svg" on:click={() => {removeFriendship(friend)}}>
            </div>
         {/each}
      </ul>
   </div>

   <h1>Send Friend Request</h1>
   <div id="friend-request-sender">
      <input type="text" bind:value={userFriendRequest}>
      <button on:click={sendFriendRequest}>Send</button>
   </div>

   <h1>Friend Requests</h1>
   <input class="filter-inputs" type="text" bind:value={friendRequestFilter}>
   <div class="list-containers">
      <ul>
         {#each filteredFriendRequests as friendRequest}
            <div class="list-element">
               <li>{friendRequest.email}</li>
               <div class="options">
                  <img src="/images/check-bold.svg" alt="" on:click={() => {acceptFriendRequest(friendRequest)}}>
                  <img src="/images/trash-can-outline.svg" on:click={() => {rejectFriendRequest(friendRequest)}}>
               </div>
            </div>
         {/each}
      </ul>
   </div>
</div>
<div id="friend-flashcardset-page" style="display: {flashcardSetsDisplay};">
   <div id="friend-flashcardset-content">
      <button id="friends-flashcardsets-close" on:click={closeFriendFlashcardSets}></button>
      <div class="list-containers" id="friends-flashcardsets-list">
      <ul>
         {#each flashcardSetsAndFavorites as flashcardSetAndFavorite}
            <div class="list-element">
               <li on:click={() => {viewFlashcards(flashcardSetAndFavorite.flashcardSet.setID, flashcardSetAndFavorite.flashcardSet.color)}}>{flashcardSetAndFavorite.flashcardSet.setName}</li>
               <img src="{flashcardSetAndFavorite.favorited ? "/images/star.svg" : "/images/star-outline.svg"}" on:click={() => {toggleFavorited(flashcardSetAndFavorite)}}>
            </div>
         {/each}
      </ul>
   </div>
   </div>
</div>
<div id="flashcardset-page" style="display: {flashcardsDisplay};">
   <button id="back-button" on:click={closeFlashcards}>Back To FlashcardSet Selection</button>
   <h1 id="flashcards-title">Flashcards</h1>
   <div class="flashcard-container">
      {#each flashcards as flashcard}
         <div class="flashcard" style="background-color: {flashcardColors};">
            <div class="flashcard-top">
               <label for="term{flashcard.flashcardID}">Term</label>
            </div>
            <textarea class="term-inputs" type="text" id="term{flashcard.flashcardID}" name="term{flashcard.flashcardID}" bind:value={flashcard.term} rows="1" readonly/>
            <label for="definition{flashcard.flashcardID}">Definition</label>
            <textarea class="definitions" id="definition{flashcard.flashcardID}" name="definition{flashcard.flashcardID}" bind:value={flashcard.definition} rows="{definitionRowCount}" readonly/>
         </div>
      {/each}
   </div>
</div>

<style>
   @import "/static/styles/main.css";

   ul {
      margin: 0;
      padding: 0;
   }

   .list-element {
      height: 30px;
      display: flex;
      justify-content: space-between;
   }

   .list-element img {
      height: 30px;
   }

   .options {
      width: 70px;
      display: flex;
      justify-content: space-between;
   }

   #friends-page {
      display: flex;
      flex-direction: column;
      align-items: center;
   }

   .list-containers {
      margin-top: 16px;
      overflow: auto;
      width: 352px;
      height: 240px;
      background-color: #F5F5F5;
      box-sizing: border-box;
      font-size: 24px;
      border: 1px solid black;
      box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
   }

   .filter-inputs {
      width: 352px;
      font-size: 24px;
   }

   li {
      height: 30px;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
      width: 100%;
   }

   #friend-request-sender {
      width: 352px;
      display: flex;
   }

   #friend-request-sender > input {
      width: 100%;
      font-size: 24px;
   }

   #friend-request-sender > button {
      margin-left: 16px;
      font-size: 24px;
   }

   #friend-flashcardset-page {
      position: fixed;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.6);
      justify-content: center;
      align-items: center;
   }

   #friend-flashcardset-content {
      width: 400px;
      height: 400px;
      background-color: #F5F5F5;
      display: flex;
      align-items: center;
      flex-direction: column;
   }

   #friends-flashcardsets-close {
      height: 40px;
      font-size: 24px;
      width: 40px;
      background: url("/images/close-box.svg");
      box-sizing: border-box;
      padding: 0;
      border: none;
      margin: 0;
      align-self: flex-end;
   }

   #friends-flashcardsets-list {
      height: 320px;
   }

   #flashcardset-page {
      display: flex;
      flex-direction: column;
      align-items: center;
   }

   .term-inputs::-webkit-scrollbar, .definitions::-webkit-scrollbar {
      width: 8px;
      border-radius: 10px;
   }

   .term-inputs::-webkit-scrollbar-track, .definitions::-webkit-scrollbar-track {
      box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
      border-radius: 10px;
   }

   .term-inputs::-webkit-scrollbar-thumb, .definitions::-webkit-scrollbar-thumb {
      background-color: var(--primary-bg-color);
      border-radius: 10px;
      outline: 1px solid black;
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

   .flashcard-top {
      display: flex;
      justify-content: space-between;
   }

   #back-button {
      height: 115px;
      font-size: 24px;
      margin-top: 20px;
      margin-bottom: 20px;
      border-radius: 10px;
      background-color: #F5F5F5;
      border-color: #BBBBBB;
      border-width: 1px;
      font-weight: 500;
      text-align: center;
   } 

   @media screen and (min-width: 769px) {
      .flashcard {
         width: clamp(600px, 70vw, 800px);
      }

      #flashcard-set-title {
         font-size: 32px;
      }

      .flashcard, .term-inputs, .definitions {
         font-size: 24px;
      }
   }
</style>