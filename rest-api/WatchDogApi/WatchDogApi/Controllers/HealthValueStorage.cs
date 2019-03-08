using System;
using System.Collections.Generic;

namespace WatchDogApi.Controllers
{
    public static class HealthValueStorage
    {
        private static readonly object Mutex = new object();
        
        private static readonly Dictionary<long, string> HeartRateValues = new Dictionary<long, string>();
        public static void AddNewValue(string value)
        {
            lock (Mutex)
            {
                var timestamp = DateTimeOffset.UtcNow.ToUnixTimeSeconds();
                HeartRateValues.Add(timestamp, value);
            }
        }

        public static Dictionary<long, string> GetHealthValues()
        {
            lock (Mutex)
            {
                var valuesToReturn = new Dictionary<long, string>(HeartRateValues);
                HeartRateValues.Clear();
                return valuesToReturn;
            }
        }
        
        public static void ClearStorage()
        {
            lock (Mutex)
            {
                HeartRateValues.Clear();
            }
        }
        
        public static Dictionary<long, string> GetDebug()
        {
            lock (Mutex)
            {
                return HeartRateValues;
            }
        }
    }
}