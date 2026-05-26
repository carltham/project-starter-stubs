import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { UserAPIService } from './user-api.service';

const testUrl = '/api/users';
describe(UserAPIService.name, () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
      // Add here declaration of your services or components and use inject to get to them in tests
      providers: [provideHttpClient(), provideHttpClientTesting(), UserAPIService],
    });
  });

  it('should request data from server', fakeAsync(
    inject(
      [HttpTestingController, UserAPIService],
      (
        httpMock: HttpTestingController, // this part will help us in testing
        service: UserAPIService // tested service
      ) => {
        // Arrange
        let result: any = 'no one expects spanish inquisition';
        const data = { some: 'data', wasMocked: true };
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
});
