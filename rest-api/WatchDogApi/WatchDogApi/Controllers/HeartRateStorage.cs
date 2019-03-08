using System;
using System.Collections.Generic;

namespace WatchDogApi.Controllers
{
    public sealed class HeartRateStorage
    {
        private static readonly object Mutex = new object();
        
        private static readonly Dictionary<long, string> HeartRateValues = new Dictionary<long, string>();
        
        private static readonly Lazy<HeartRateStorage> Lazy = new Lazy<HeartRateStorage>(() => new HeartRateStorage());

        public static HeartRateStorage Instance => Lazy.Value;

        private HeartRateStorage()
        {
        }

        public static void AddNewValue(string value)
        {
            lock (Mutex)
            {
                var timestamp = DateTimeOffset.UtcNow.ToUnixTimeSeconds();
                HeartRateValues.Add(timestamp, value);
            }
        }

        public static Dictionary<long, string> GetHeartRateValues()
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
    }
}