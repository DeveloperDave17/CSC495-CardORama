<script>
   import { GlobalReferences } from "$lib/globals";
   import { onMount } from "svelte";
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
            .setDeveloperKey("AIzaSyCnY7IiNRns8IA5-2p88s4on1-D4NYIAw8")
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
         fetch(globals.backendBasePath + `/GoogleDrive/loadFlashcardSet/${fileId}`, {
               mode: "same-origin",
               method: "post",
               credentials: "include",
            }).then((response) => {
               console.log(response.status);
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
   })
</script>

<button on:click={openPicker}>Test</button>