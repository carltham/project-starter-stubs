import { HttpClient } from "@angular/common/http";
import {
  HttpClientTestingModule,
  HttpTestingController,
} from "@angular/common/http/testing";
import { TestBed, fakeAsync, inject, tick } from "@angular/core/testing";
import { Data } from "@angular/router";
import { UserAPIService } from "./services/user-api.service";

const testUrl = "/api/users";
describe("HttpClient testing", () => {
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
    });

    // Inject the http service and test controller for each test
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  });
  /// Tests begin ///
  it("can test HttpClient.get", () => {
    const testData: Data = { name: "Test Data" };

    // Make an HTTP GET request
    httpClient.get<Data>(testUrl).subscribe((data) =>
      // When observable resolves, result should match test data
      expect(data).toEqual(testData)
    );

    // The following `expectOne()` will match the request's URL.
    // If no requests or multiple requests matched that URL
    // `expectOne()` would throw.
    const req = httpTestingController.expectOne(testUrl);

    // Assert that the request is a GET.
    expect(req.request.method).toEqual("GET");

    // Respond with mock data, causing Observable to resolve.
    // Subscribe callback asserts that correct data was returned.
    req.flush(testData);

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();
  });
});

beforeEach(() => {
  TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule, // angular testing module that provides mocking for http connections
    ],
    // Add here declaration of your services or components and use inject to get to them in tests
    providers: [UserAPIService],
  });
});

it("should request data from server", fakeAsync(
  inject(
    [HttpTestingController, UserAPIService],
    (
      httpMock: HttpTestingController, // this part will help us in testing
      service: UserAPIService // tested service
    ) => {
      // Arrange
      let result: any = "no one expects spanish inquisition";
      const data = { some: "data", wasMocked: true };
      const expected = { ...data, wasMocked: true }; // or whatever your data service does with returned data

      // Act
      service
        .getAll()
        // we just care about what we get in the end
        .subscribe((data) => (result = data));

      // Assert
      httpMock.expectOne(testUrl).flush(data);
      tick();

      expect(result).toEqual(expected); // after processing server response
      // check if there arent any other not handled requests
      httpMock.verify();
    }
  )
));
