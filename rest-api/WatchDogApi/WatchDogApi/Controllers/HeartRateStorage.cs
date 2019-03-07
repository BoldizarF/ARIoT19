using System;
using System.Collections.Generic;

namespace WatchDogApi.Controllers
{
    public sealed class HeartRateStorage
    {
        private static readonly object mutex = new object();
        
        private static readonly Dictionary<long, string> _heartRateValues = new Dictionary<long, string>();
        
        private static readonly Lazy<HeartRateStorage> Lazy = new Lazy<HeartRateStorage>(() => new HeartRateStorage());

        public static HeartRateStorage Instance => Lazy.Value;

        private HeartRateStorage()
        {
        }

        public static void AddNewValue(string value)
        {
            lock (mutex)
            {
                var timestamp = DateTimeOffset.UtcNow.ToUnixTimeSeconds();
                _heartRateValues.Add(timestamp, value);
            }
        }

        public static Dictionary<long, string> GetHeartRateValues()
        {
            lock (mutex)
            {
                return _heartRateValues;
            }
        }
        
        public static void ClearStorage()
        {
            lock (mutex)
            {
                _heartRateValues.Clear();
            }
        }
    }
}