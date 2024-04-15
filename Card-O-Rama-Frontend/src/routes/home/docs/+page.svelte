<script>
   import { GlobalReferences } from "$lib/globals";
   import { onMount } from "svelte";
   import Swal from "sweetalert2";
   import { PUBLIC_DOC_API_KEY } from "$env/static/public";

   let globals = new GlobalReferences();
   function test() {
      fetch(globals.backendBasePath + `/GoogleDrive/info`, {
               mode: "same-origin",
               method: "get",
               credentials: "include"
            }).then((response) => {
               console.log(response)
               return response.json();
            }).then((data) => {
               console.log(data);
            })
   }

   let pickerInited = false;
   let loadingDisplay = "none";

   function onPickerApiLoad() {
      pickerInited = true;
   }

   function openPicker() {
      if (pickerInited) {
      fetch(globals.backendBasePath + `/OAuthUtil/getToken`, {
               mode: "same-origin",
               method: "get",
               credentials: "include",
            }).then((response) => {
               return response.text();
            }).then((data) => {
               console.log(data);
               createPicker(data);
            });
         }
   }

   function createPicker(accessToken) {
      const showPicker = () => {
         const picker = new google.picker.PickerBuilder()
            .addView(google.picker.ViewId.DOCUMENTS)
            .setOAuthToken(accessToken)
            .setDeveloperKey(PUBLIC_DOC_API_KEY)
            .setCallback(pickerCallBack)
            .build();
         picker.setVisible(true);
      }

      showPicker();
   }

   async function pickerCallBack(data) {
      if (data.action === google.picker.Action.PICKED) {
         const document = data[google.picker.Response.DOCUMENTS][0];
         const fileId = document[google.picker.Document.ID];
         console.log(fileId);
         loadingDisplay = "flex";
         fetch(globals.backendBasePath + `/GoogleDrive/loadFlashcardSet/${fileId}`, {
               mode: "same-origin",
               method: "post",
               credentials: "include",
            }).then((response) => {
               console.log(response.status);
               loadingDisplay = "none";
               Swal.fire({
                  title: "Flashcard Set Creation Success!",
                  icon: "success"
               });
            })
      }
   }

   onMount(() => {
      const googlePicker = document.createElement('script');
      googlePicker.onload = () => {
         if (gapi) {
            gapi.load('picker', onPickerApiLoad);
            console.log("available");
         }
      };
      googlePicker.src = "https://apis.google.com/js/api.js";
      document.head.appendChild(googlePicker);
   });
</script>

<div id="page">
   <div id="page-content">
      <img src="/images/drive_logo.png" alt="Google Drive Logo">
      <p>Select a document to parse into flashcards. Make sure all of the desired terms and defintions are seperated
         by the same deliminators. Allowed deliminators include colon(:), semi-colon(;), comma(,), hyphen(-), and is.
         The colon symbol is recommended as it takes priority over other symbols.
      </p>
      <button on:click={openPicker}>Select Document</button>
   </div>
</div>
<div id="loading-page" style="display: {loadingDisplay};">
   <div id="loading-content">
      <img id="loading-img" src="/images/loading.svg" alt="loading">
   </div>
</div>

<style>
   @import "/static/styles/main.css";


   #page {
      display: flex;
      justify-content: center;
   }

   #page-content {
      max-width: 800px;
      min-width: 600px;
      display: flex;
      flex-direction: column;
      margin-top: 100px;
      justify-content: center;
      align-items: center;
      border: 2px solid var(--primary-bg-color);
      padding: 25px;
   }

   p {
      font-size: 28px;
      color: var(--primary-bg-color);
      font-weight: 500;
      text-align: center;
   }

   button {
      width: 400px;
      height: 57px;
      font-size: 28px;
      background-color: var(--primary-bg-color);
      color: white;
   }

   img {
      width: 160px;
   }

   #loading-page {
      position: fixed;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.6);
      justify-content: center;
      align-items: center;
   }

   #loading-content {
      background-color: white;
      box-sizing: border-box;
      padding: 20px;
      width: 200px;
      height: 200px;
   }

   #loading-img {
      animation: spin 1s linear infinite;
   }

   @keyframes spin {
      0% {
         transform: rotate(0deg);
      }
      100% {
         transform: rotate(360deg);
      }
   }
</style>