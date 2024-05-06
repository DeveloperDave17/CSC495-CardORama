import { PUBLIC_REVERSE_PROXY_PORT } from "$env/static/public";
import { PUBLIC_BACKEND_PORT } from "$env/static/public";

const frontendPort = PUBLIC_REVERSE_PROXY_PORT;
const backendPort = PUBLIC_REVERSE_PROXY_PORT;

export class GlobalReferences {
   constructor() {
      this.indexlocation = `http://localhost:${frontendPort}`;
      this.backendBasePath = `http://localhost:${backendPort}/api`;
   }
}