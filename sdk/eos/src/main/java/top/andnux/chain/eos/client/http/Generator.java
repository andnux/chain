package top.andnux.chain.eos.client.http;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import top.andnux.chain.eos.api.result.error.ApiError;
import top.andnux.chain.eos.client.exception.ApiException;


public class Generator {

	private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

	private static Retrofit.Builder builder = new Retrofit.Builder();

	private static Retrofit retrofit;

	public static <S> S createService(Class<S> serviceClass, String baseUrl) {
		httpClientBuilder.addInterceptor(chain -> {
			Request original = chain.request();
			Request request = original.newBuilder()
					.method(original.method(), original.body())
					.build();

			return chain.proceed(request);
		});

		retrofit = builder.baseUrl(baseUrl).client(httpClientBuilder.build())
				.addConverterFactory(JacksonConverterFactory.create()).build();
		return retrofit.create(serviceClass);
	}

	public static <T> T executeSync(Call<T> call) throws ApiException, IOException {
		Response<T> response = call.execute();
		if (response.isSuccessful()) {
			return response.body();
		} else {
			ApiError apiError = getApiError(response);
			throw new ApiException(apiError);
		}
	}

	private static ApiError getApiError(Response<?> response) throws IOException, ApiException {
		return (ApiError) retrofit.responseBodyConverter(ApiError.class, new Annotation[0])
				.convert(response.errorBody());
	}
}
