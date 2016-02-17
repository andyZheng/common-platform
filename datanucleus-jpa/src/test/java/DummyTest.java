import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.datanucleus.samples.entity.Book;
import com.datanucleus.samples.entity.Inventory;
import com.datanucleus.samples.entity.Product;
import com.datanucleus.samples.entity.Remarks;
import com.datanucleus.samples.service.DummyService;

public class DummyTest {

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:/spring/*-test.xml")) {
            DummyService testService = ac.getBean(DummyService.class);
            // 清理上一次数据
            testService.removeAll();

            for (int i = 1; i < 6; i++) {
                // 创建库存实例
                Inventory inv = new Inventory("Book-" + i);
                // 创建产品
                Product p = new Product("西游" + i, "唐僧取经", 29);
                inv.getProducts().add(p);
                // 创建书籍
                Book b = new Book("幸福生活" + String.valueOf(i), "UFO", 36, "andy.zheng", "isbn", "publisher");
                inv.getProducts().add(b);

                // 备注信息
                Remarks remarks = new Remarks();
                remarks.setName(String.valueOf(i));
                remarks.setContent("content");
                inv.setRemarks(remarks);
                
                inv = testService.service(inv);
                System.err.println(testService.queryById(inv.getName()));
            }
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }
}
