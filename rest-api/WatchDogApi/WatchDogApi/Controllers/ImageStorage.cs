using System;
using System.Collections;
using System.Collections.Generic;

namespace WatchDogApi.Controllers
{
    public sealed class ImageStorage
    {
        private static readonly object mutex = new object();
        
        private static readonly Dictionary<long, string> Images = new Dictionary<long, string>();
        
        private static readonly Lazy<ImageStorage> Lazy = new Lazy<ImageStorage>(() => new ImageStorage());

        public static ImageStorage Instance => Lazy.Value;

        private ImageStorage()
        {
        }

        public static void AddImage(string encodedImage)
        {
            lock (mutex)
            {
                var timestamp = DateTimeOffset.UtcNow.ToUnixTimeSeconds();
                Images.Add(timestamp, encodedImage);
            }
        }

        public static Dictionary<long, string> GetImages()
        {
            lock (mutex)
            {
                var imagesToReturn = new Dictionary<long, string>(Images);
                Images.Clear();
                return imagesToReturn;
            }
        }
        
        public static void ClearStorage()
        {
            lock (mutex)
            {
                Images.Clear();
            }
        }
    }
}