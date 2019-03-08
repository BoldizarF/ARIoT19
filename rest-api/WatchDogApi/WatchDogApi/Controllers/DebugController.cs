using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;

namespace WatchDogApi.Controllers
{
    [Route("api/v1/[controller]")]
    [ApiController]
    public class DebugController : ControllerBase
    {
        
        [HttpGet]
        public ActionResult<IDictionary<long, string>> Images()
        {
            return ImageStorage.GetDebug();
        }
        
        [HttpGet]
        public ActionResult<IDictionary<long, string>> Values()
        {
            return HealthValueStorage.GetDebug();
        }
    }
}