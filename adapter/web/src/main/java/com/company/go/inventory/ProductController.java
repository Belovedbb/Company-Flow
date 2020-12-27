package com.company.go.inventory;

import com.company.go.ControllerUtilities;
import com.company.go.Utilities;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.exceptions.ProductNotStoredException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/inventory/product")
public class ProductController {

    private IndexUseCase indexer;

    private ProductUseCase productUseCase;


    public ProductController(IndexUseCase indexer, ProductUseCase productUseCase) {
        this.indexer = indexer;
        this.productUseCase = productUseCase;
    }

    @GetMapping
    public String setUpProduct(Model model) throws IOException, SQLException {
        ProductUseCase.ProductViewModel product = new ProductUseCase.ProductViewModel();
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("state", Utilities.State.NEW.name());
        model.addAttribute("product", product);
        model.addAttribute("productTypes", productUseCase.getProductCategory());
        return "inventory/product/product-form";
    }

    @GetMapping("/listing")
    public String setUpProductListing(Model model) throws IOException, SQLException {
        ProductUseCase.ProductViewModel product = new ProductUseCase.ProductViewModel();
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("product", product);
        model.addAttribute("products", productUseCase.getAllProducts());
        return "inventory/product/product-listing";
    }

    @PostMapping("/listing/filter")
    public String getFilteredProductListing(Model model, ProductUseCase.ProductViewModel product){
        model.addAttribute("products", productUseCase.getFilteredProducts(product));
        return "inventory/product/product-listing :: result";
    }

    @PostMapping
    public String saveNewProduct(Model model, @ModelAttribute("product")  @Valid ProductUseCase.ProductViewModel product,
                                    BindingResult result) throws IOException, SQLException, ProductNotStoredException {
        if(result.hasErrors()){
            ControllerUtilities.storeIndexDetailsInModel(model, indexer);
            model.addAttribute("state", product.getId() == null ? Utilities.State.NEW.name() : Utilities.State.EDIT.name());
            model.addAttribute("productTypes", productUseCase.getProductCategory());
            return "inventory/product/product-form";
        }
        if( product.getId() == null){
            Long previousStoredId = productUseCase.getProductMaxId();
            boolean isStored = productUseCase.storeProduct(product);
            if(isStored){
                Long storedProductId = productUseCase.getProductMaxId();
                storedProductId = storedProductId != null ? storedProductId : -3 >> 1;
                if(previousStoredId != null && (storedProductId <= previousStoredId ) )
                    throw new ProductNotStoredException("This product was not saved successfully");
                return "redirect:/inventory/product/view/"+storedProductId;
            }
        }else{
            boolean updated = productUseCase.editProduct(product.getId(), product);
            if(!updated){
                throw new ProductNotStoredException("This product was not updated successfully");
            }
            return "redirect:/inventory/product/view/"+product.getId();
        }
        return "redirect:/inventory/product";
    }

    @GetMapping("/view/{id}")
    public String viewProduct(@PathVariable Long id, Model model) throws IOException, SQLException {
        ProductUseCase.ProductViewModel product = productUseCase.viewProduct(id);
        model.addAttribute("state", Utilities.State.VIEW.name());
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("productTypes", productUseCase.getProductCategory());
        model.addAttribute("product", product);
        return "inventory/product/product-form";
    }

    @GetMapping("/edit/{id}")
    public String setUpEditProduct(@PathVariable Long id, Model model) throws IOException, SQLException {
        ProductUseCase.ProductViewModel product = productUseCase.viewProduct(id);
        model.addAttribute("state", Utilities.State.EDIT.name());
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("productTypes", productUseCase.getProductCategory());
        model.addAttribute("product", product);
        return "inventory/product/product-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) throws Exception {
        boolean isDeleted = productUseCase.deleteProduct(id);
        if(!isDeleted){
            throw new Exception();
        }
        return "redirect:/inventory/product/listing";
    }

    @GetMapping("/report")
    public String setUpProductReport(Model model) throws IOException, SQLException {
        return "redirect:/report/inventory/product";
    }

}
