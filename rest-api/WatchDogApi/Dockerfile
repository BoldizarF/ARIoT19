FROM microsoft/dotnet:2.2-sdk AS build
WORKDIR /app

# copy csproj and restore as distinct layers
COPY *.sln .
COPY WatchDog/*.csproj ./WatchDog/
RUN dotnet restore

# copy everything else and build app
COPY WatchDog/. ./WatchDog/
WORKDIR /app/WatchDog
RUN dotnet publish -c Release -o out

FROM microsoft/dotnet:2.2-aspnetcore-runtime AS runtime
WORKDIR /app
COPY --from=build /app/WatchDog/out ./

EXPOSE 80
ENTRYPOINT ["dotnet", "WatchDog.dll"]

# docker build -t watchdog.api.v1 .
# docker images ls

# docker run -d -p 8080:80 --name watchdog watchdog.api.v1
# localhost:8080/api/values
