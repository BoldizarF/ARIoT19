using System;
using System.Collections;
using System.Collections.Generic;

namespace WatchDogApi.Storage
{
    public static class ImageStorage
    {
        private static readonly object Mutex = new object();
        
        private static readonly Dictionary<long, string> Images = new Dictionary<long, string>();
        
        public static void AddImage(string encodedImage)
        {
            lock (Mutex)
            {
                var timestamp = DateTimeOffset.UtcNow.ToUnixTimeSeconds();
                Images.Add(timestamp, encodedImage);
            }
        }

        public static Dictionary<long, string> GetImages()
        {
            lock (Mutex)
            {
                var imagesToReturn = new Dictionary<long, string>(Images);
                Images.Clear();
                return imagesToReturn;
            }
        }
        
        public static void ClearStorage()
        {
            lock (Mutex)
            {
                Images.Clear();
            }
        }
        
        public static Dictionary<long, string> GetDebug()
        {
            lock (Mutex)
            {
                return Images;
            }
        }
    }
}