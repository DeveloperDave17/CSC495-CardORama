<script>
	import { goto } from "$app/navigation";
   import { GlobalReferences } from "$lib/globals";
	import { onMount } from "svelte";
   import Swal from "sweetalert2";

   // friend display logic
   let friends = [{friendEmail: "bob@yahoo.com"}, {friendEmail: "notBob@yahoo.com"}];
   let friendFilter = "";
   $: filteredFriends = friends.filter((friend) => friend.friendEmail.includes(friendFilter));

   let userFriendRequest = "";

   // pending friend requests display logic
   let friendRequests = [{email: "bobby@yahoo.com"}, {email: "notBobby@yahoo.com"}];
   let friendRequestFilter = "";
   $: filteredFriendRequests = friendRequests.filter((friendRequest) => friendRequest.email.includes(friendRequestFilter));

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
</script>

<div id="friends-page">
   <h1>Friends</h1>
   <input class="filter-inputs" type="text" bind:value={friendFilter}>
   <div class="list-containers">
      <ul>
         {#each filteredFriends as friend}
            <div class="list-element">
               <li>{friend.friendEmail}</li>
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
</style>