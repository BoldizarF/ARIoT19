using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

namespace WatchDogApi.Storage
{
    public static class ActiveClientStorage
    {
        private static readonly Random Random = new Random();
        private static readonly object Mutex = new object();
        
        private static readonly List<string> Clients = new List<string>();
        
        public static string AddClient()
        {
            lock (Mutex)
            {
                var apiKey = GenerateApiKey();
                Clients.Add(apiKey);
                return apiKey;
            }
        }

        public static bool IsClientAccepted(string apiKey)
        {
            lock (Mutex)
            {
                return Clients.Any(x => x == apiKey);
            }
        }

        public static void Clear()
        {
            lock (Mutex)
            {
                Clients.Clear();
            }
        }
        
        public static List<string> GetDebug()
        {
            lock (Mutex)
            {
                return Clients;
            }
        }

        private static string GenerateApiKey()
        {
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            return new string(Enumerable.Repeat(chars, 15)
                .Select(s => s[Random.Next(s.Length)]).ToArray());
        }

    }
}