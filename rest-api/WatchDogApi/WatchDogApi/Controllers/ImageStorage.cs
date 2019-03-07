using System;
using System.Collections.Generic;

namespace WatchDogApi.Controllers
{
    public sealed class ImageStorage
    {
        private static readonly Dictionary<long, string> Images = new Dictionary<long, string>();
        
        private static readonly Lazy<ImageStorage> Lazy = new Lazy<ImageStorage>(() => new ImageStorage());

        public static ImageStorage Instance => Lazy.Value;

        private ImageStorage()
        {
        }

        public static void AddImage(string encodedImage)
        {
            var timestamp = DateTimeOffset.UtcNow.ToUnixTimeSeconds();
            Images.Add(timestamp, encodedImage);
        }

        public static Dictionary<long, string> GetImages()
        {
            return Images;
        }
        
        public static void ClearStorage()
        {
            Images.Clear();
        }
    }
}