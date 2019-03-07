using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace WatchDogApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ValuesController : ControllerBase
    {
        // GET api/values
        [HttpGet]
        public ActionResult<IDictionary<long, string>> Get()
        {
            return ImageStorage.GetImages();
        }

        // POST api/values
        [HttpPost]
        public void Post([FromBody] string encodedImage)
        {
            ImageStorage.AddImage(encodedImage);
        }

        // DELETE api/values/
        [HttpDelete]
        public void Delete()
        {
            ImageStorage.ClearStorage();
        }
    }
}