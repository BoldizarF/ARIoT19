using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using WatchDogApi.Storage;

namespace WatchDogApi.Controllers
{
    [Route("api/v1/[controller]")]
    [ApiController]
    public class ImagesController : ControllerBase
    {
        [HttpGet]
        public ActionResult<IDictionary<long, string>> Get([FromQuery(Name = "apikey")] string apikey)
        {
            if (!ActiveClientStorage.IsClientAccepted(apikey))
            {
                return Forbid();
            }

            return ImageStorage.GetImages();
        }

        [HttpPost]
        public ActionResult Post([FromBody] string encodedImage, [FromQuery(Name = "apikey")] string apikey)
        {
            if (apikey != "666")
            {
                return Forbid();
            }

            ImageStorage.AddImage(encodedImage);
            return Ok();
        }

        [HttpDelete]
        public ActionResult Delete([FromQuery(Name = "apikey")] string apikey)
        {
            if (apikey != "12345")
            {
                return Forbid();
            }
            
            ImageStorage.ClearStorage();
            return Ok();
        }
    }
}