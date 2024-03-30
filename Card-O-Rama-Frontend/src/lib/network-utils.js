import { GlobalReferences } from "./globals";
const globalReferences = new GlobalReferences();

/**
 * A function that sends a post request to a given url with json data. The response is returned unless either the user is unauthorized or
 * an error occurs. If a user is unauthorized then they are redirected to the index page.
 * @param {String} url The target url for the post request.
 * @param {String} jsonData A stringified json object.
 */
export async function fetchPost(url, jsonData) {
   await fetch(url, {
      mode: "cors",
      method: "post",
      headers: {
         "Content-Type" : "application/json"
      },
      body: jsonData,
      credentials: "include"
   }).then((response) => {
      if (response.status === 200) {
         return response;
      } else {
         // User not authorized, redirect user to login.
         // window.location.href = globalReferences.indexlocation;
      }
   }).catch((error) => {
      console.log(`An error occurred when attempting to fetch a post request.\n${error}`);
   })
}

/**
 * A function that sends a get request to a given url. The response is returned unless either the user is unauthorized or
 * an error occurs. If a user is unauthorized then they are redirected to the index page.
 * @param {String} url
 * @returns {Response | null} [response] 
 */
export async function fetchGet(url) {
   await fetch(url, {
      method: "get",
      credentials: "include"
   }).then((response) => {
      if (response.status === 200) {
         return response;
      } else {
         // User not authorized, redirect user to login.
         window.location.href = globalReferences.indexlocation;
      }
   }).catch((error) => {
      console.log(`An error occurred when attempting to fetch a get request.\n${error}`);
   })
   return null;
}