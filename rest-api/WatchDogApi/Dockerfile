FROM microsoft/dotnet:2.2-sdk AS build
WORKDIR /app

# copy csproj and restore as distinct layers
COPY *.sln .
COPY WatchDogApi/*.csproj ./WatchDogApi/
RUN dotnet restore

# copy everything else and build app
COPY WatchDogApi/. ./WatchDogApi/
WORKDIR /app/WatchDogApi
RUN dotnet publish -c Release -o out

FROM microsoft/dotnet:2.2-aspnetcore-runtime AS runtime
WORKDIR /app
COPY --from=build /app/WatchDogApi/out ./

EXPOSE 80
ENTRYPOINT ["dotnet", "WatchDogApi.dll"]

# docker build -t watchdog.api.v1 .
# docker images ls

# docker run -d -p 8080:80 --name watchdog watchdog.api.v1
# localhost:8080/api/values
