import com.imooc.service.impl.TestTransServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/*@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)*/
public class TransTest {
    @Autowired
    private TestTransServiceImpl testTransService;
    /*
    * 事务传播特性
    *   required：
    *   supports：
    *   mandatory：
    *   required_new:
    *   not_supported:
    *   never:
    *   nested:
    * */

}
