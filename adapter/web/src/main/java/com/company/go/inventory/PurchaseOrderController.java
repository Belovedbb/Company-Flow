package com.company.go.inventory;

import com.company.go.ControllerUtilities;
import com.company.go.Utilities;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.exceptions.PurchaseOrderNotStoredException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/inventory/purchase/order")
public class PurchaseOrderController {

    private PurchaseOrderUseCase purchaseOrderUseCase;
    private IndexUseCase indexer;

    public PurchaseOrderController(PurchaseOrderUseCase purchaseOrderUseCase, IndexUseCase indexer){
        this.indexer = indexer;
        this.purchaseOrderUseCase = purchaseOrderUseCase;
    }

    @GetMapping
    public String setUpPurchaseOrder(Model model) throws IOException, SQLException {
        PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder = new PurchaseOrderUseCase.PurchaseOrderViewModel();
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("state", Utilities.State.NEW.name());
        model.addAttribute("purchaseOrder", purchaseOrder);
        model.addAttribute("loadedStatus", purchaseOrder.getLoadedStatus());
        model.addAttribute("availableProducts", purchaseOrderUseCase.getAvailableProducts());
        return "inventory/purchaseorder/purchaseorder-form";
    }

    @GetMapping("/listing")
    public String setUpPurchaseOrderListing(Model model) throws IOException, SQLException {
        PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder = new PurchaseOrderUseCase.PurchaseOrderViewModel();
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("purchaseOrder", purchaseOrder);
        model.addAttribute("purchaseOrders", purchaseOrderUseCase.getAllPurchaseOrders());
        return "inventory/purchaseorder/purchaseorder-listing";
    }

    @PostMapping("/listing/filter")
    public String getFilteredPurchaseOrderListing(Model model, PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder){
        return "";
    }

    @PostMapping("/addRemoveOrderEntry")
    public String getAddOrderEntry(@RequestParam("productIds") Long[] productIds, @RequestParam("operation") Integer operation,
                                   @ModelAttribute("purchaseOrder")  PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder){
        if (operation == 0) {
            purchaseOrderUseCase.saveOrderEntry(purchaseOrder, productIds);
        } else {
            purchaseOrderUseCase.removeOrderEntry(purchaseOrder, productIds);
        }
        return "inventory/purchaseorder/purchaseorder-form :: result";
    }

    @PostMapping("/displayEntryAmount")
    public String getEntryAmount(@RequestParam("quantityValue") Long entryQuantity, @RequestParam("indexValue") Long indexValue,
                                   @ModelAttribute("purchaseOrder")  PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder){
        purchaseOrderUseCase.updateOrderAmountEntry(purchaseOrder, entryQuantity, indexValue);
        return "inventory/purchaseorder/purchaseorder-form :: result";
    }

    @PostMapping
    public String saveNewPurchaseOrder(Model model, @ModelAttribute("purchaseOrder")  @Valid PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder,
                                 BindingResult result) throws IOException, SQLException, PurchaseOrderNotStoredException {
        //TODO impl empty element check for entry
        if(result.hasErrors()){
            ControllerUtilities.storeIndexDetailsInModel(model, indexer);
            model.addAttribute("state", purchaseOrder.getId() == null ? Utilities.State.NEW.name() : Utilities.State.EDIT.name());
            model.addAttribute("status", purchaseOrder.getLoadedStatus());
            model.addAttribute("availableProducts", purchaseOrderUseCase.getAvailableProducts());
            return "inventory/purchaseorder/purchaseorder-form";
        }
        if( purchaseOrder.getId() == null){
            Long previousStoredId = purchaseOrderUseCase.getPurchaseOrderMaxId();
            boolean isStored = purchaseOrderUseCase.storePurchaseOrder(purchaseOrder);
            if(isStored){
                Long storedPurchaseOrderId = purchaseOrderUseCase.getPurchaseOrderMaxId();
                storedPurchaseOrderId = storedPurchaseOrderId != null ? storedPurchaseOrderId : -3 >> 1;
                if(previousStoredId != null && (storedPurchaseOrderId <= previousStoredId ) )
                    throw new PurchaseOrderNotStoredException("This order was not saved successfully");
                return "redirect:/inventory/purchase/order/view/"+storedPurchaseOrderId;
            }
        }else{
            boolean updated = purchaseOrderUseCase.editPurchaseOrder(purchaseOrder.getId(), purchaseOrder);
            if(!updated){
                throw new PurchaseOrderNotStoredException("This order was not updated successfully");
            }
            return "redirect:/inventory/purchase/order/view/"+ purchaseOrder.getId();
        }
        return "redirect:/inventory/purchase/order";
    }

    @GetMapping("/view/{id}")
    public String viewPurchaseOrder(@PathVariable Long id, Model model) throws IOException, SQLException {
        PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder = purchaseOrderUseCase.viewPurchaseOrder(id);
        model.addAttribute("state", Utilities.State.VIEW.name());
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("purchaseOrder", purchaseOrder);
        model.addAttribute("loadedStatus", purchaseOrder.getLoadedStatus());
        model.addAttribute("availableProducts", purchaseOrderUseCase.getAvailableProducts());
        return "inventory/purchaseorder/purchaseorder-form";
    }

    @GetMapping("/edit/{id}")
    public String setUpEditPurchaseOrder(@PathVariable Long id, Model model) throws IOException, SQLException {
        PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder = purchaseOrderUseCase.viewPurchaseOrder(id);
        model.addAttribute("state", Utilities.State.EDIT.name());
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("purchaseOrder", purchaseOrder);
        model.addAttribute("loadedStatus", purchaseOrder.getLoadedStatus());
        model.addAttribute("availableProducts", purchaseOrderUseCase.getAvailableProducts());
        return "inventory/purchaseorder/purchaseorder-form";
    }

    @GetMapping("/delete/{id}")
    public String deletePurchaseOrder(@PathVariable Long id) throws Exception {
        boolean isDeleted = purchaseOrderUseCase.deletePurchaseOrder(id);
        if(!isDeleted){
            throw new Exception();
        }
        return "redirect:/inventory/purchase/order/listing";
    }

    @GetMapping("/report")
    public String setUpPurchaseOrderReport(Model model) throws IOException, SQLException {
        return "redirect:/report/inventory/purchase/order";
    }
}
