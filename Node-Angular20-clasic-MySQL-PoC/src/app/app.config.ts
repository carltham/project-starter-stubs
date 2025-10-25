import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection,
} from "@angular/core";
import { provideRouter } from "@angular/router";

import { provideHttpClient, withFetch } from "@angular/common/http";
import {
  provideClientHydration,
  withEventReplay,
  withHttpTransferCacheOptions,
} from "@angular/platform-browser";
import { routes } from "./app.routes";

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideClientHydration(
      withEventReplay(),
      withHttpTransferCacheOptions({ includePostRequests: false })
    ),
    provideHttpClient(withFetch()),
  ],
};
