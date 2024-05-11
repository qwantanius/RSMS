package RSMS.RollingStocks.managers;

import RSMS.RollingStocks.BasicRollingStock;
import RSMS.common.exceptions.CouplingManagerException;
import RSMS.common.exceptions.RollingStockIsNotAttachedException;

public class RollingStockCouplingManager {

    private BasicRollingStock rollingStock;
    private BasicRollingStock connectedTo;
    private RollingStockCouplingPolicyManager rollingStockCouplingPolicyManager;


    public RollingStockCouplingManager(BasicRollingStock rollingStock, RollingStockCouplingPolicyManager rollingStockCouplingPolicyManager) {
        this.rollingStock = rollingStock;
        this.rollingStockCouplingPolicyManager = rollingStockCouplingPolicyManager;
    }

    public BasicRollingStock getManagableRollingStock() {
        return this.rollingStock;
    }

    public BasicRollingStock getConnectedRollingStock() {
        return this.connectedTo;
    }

    public RollingStockCouplingPolicyManager getRollingStockCouplingPolicyManager() {
        return this.rollingStockCouplingPolicyManager;
    }

    public RollingStockCouplingManager attachTo(BasicRollingStock target) throws CouplingManagerException {
        this.validateBeforeAttaching(target);
        this.connectedTo = target;
        return this;
    }

    public RollingStockCouplingManager detach() throws RollingStockIsNotAttachedException {
        this.validateBeforeDetaching();
        this.connectedTo = null;
        return this;
    }

    private boolean isUpcomingConnectionRecursive(BasicRollingStock target) {
        BasicRollingStock connectedTo = target.getCouplingManager().getConnectedRollingStock();
        return connectedTo != null && connectedTo.getUuid()  == this.rollingStock.getUuid();
    }

    public boolean isAttached() {
        return this.connectedTo != null;
    }

    private void validateBeforeAttaching(BasicRollingStock target) throws CouplingManagerException {
        if (target == null) {
            throw new CouplingManagerException("Cannot connect if target is null\n trace: " + this.getManagableRollingStock());
        }
        if (this.isAttached()) {
            throw new CouplingManagerException("Already attached: " + this.getManagableRollingStock().getUuid());
        }
        if (target.getUuid() == this.getManagableRollingStock().getUuid()) {
            throw new CouplingManagerException("Cannot attach target to itself: " + this.getManagableRollingStock().getUuid());
        }
        if (this.isUpcomingConnectionRecursive(target)) {
            throw new CouplingManagerException(
                String.format(
                    "Caused recursive connection: %s -/-> %s",
                    this.getManagableRollingStock().getUuid(),
                    target.getUuid()
                )
            );
        }
        BasicRollingStock startValidationFrom = target;
    }

    private void validateBeforeDetaching() throws RollingStockIsNotAttachedException {
        if (!this.isAttached()) {
            throw new RollingStockIsNotAttachedException(this.rollingStock);
        }
    }
}
