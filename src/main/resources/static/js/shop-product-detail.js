document.addEventListener('DOMContentLoaded', function() {
    const priceElement = document.getElementById('product-price');
    const quantityElement = document.getElementById('quantity');
    const totalPriceElement = document.getElementById('total-price');

    // 가격 정보 가져오기
    const productPrice = parseFloat(priceElement.textContent);

    // 수량이 변경될 때 총 가격 업데이트
    quantityElement.addEventListener('input', function() {
        const quantity = parseInt(quantityElement.value);
        const totalPrice = productPrice * quantity;
        totalPriceElement.textContent = totalPrice.toFixed(2);
    });
});

/*
@Controller - Spring Controller 업데이트 - 가격 정보를 가져오기 위해 컨트롤러 업데이트.
public class ProductController {

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") Long id, Model model) {
        // 예: 가격을 데이터베이스에서 가져오기
        double price = priceService.getPriceById(id);

        // 모델에 가격 추가
        model.addAttribute("price", price);

        // 다른 필요한 모델 속성 추가

        return "single-product"; // Thymeleaf 템플릿 이름
    }
}

@Service - 서비스 업데이트 : 가격 정보를 가져오는 서비스 레이어를 구현
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public double getPriceById(Long id) {
        // 예: 데이터베이스에서 가격을 가져오기
        Optional<Price> priceOptional = priceRepository.findById(id);
        if (priceOptional.isPresent()) {
            return priceOptional.get().getPrice();
        } else {
            throw new NoSuchElementException("Product not found");
        }
    }
}


@Repository -  가격 정보를 저장하는 리포지토리를 구현한다.. repository 업데이트
public interface PriceRepository extends JpaRepository<Price, Long> {
    // 필요한 쿼리 메소드 추가
}


@Entity - Model 업데이트 / 가격 정보를 저장하는 엔터티를 구현한다.
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double price;

    // Getter와 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

*/
