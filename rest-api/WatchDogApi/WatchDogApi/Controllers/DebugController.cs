using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;

namespace WatchDogApi.Controllers
{
    [Route("api/v1/[controller]")]
    [ApiController]
    public class DebugController : ControllerBase
    {
        
        [HttpGet("images")]
        public ActionResult<IDictionary<long, string>> Images()
        {
            return ImageStorage.GetDebug();
        }
        
        [HttpGet("values")]
        public ActionResult<IDictionary<long, string>> Values()
        {
            return HealthValueStorage.GetDebug();
        }
    }
}