// package com.logistic.logistic_engine.repository;

// import com.logistic.logistic_engine.entity.Order;
// import com.logistic.logistic_engine.enums.OrderStatus;

// import org.springframework.data.domain.Page;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;

// import java.util.List;


// public interface OrderRepository extends JpaRepository<Order, Long> {

//     @Query(value = "SELECT * FROM Orders WHERE agent_id = ?1 AND status = ?2 ORDER BY created_at DESC LIMIT ?4 OFFSET ?3", nativeQuery = true)
//     Page<Order> findAllCustomOrdersByAgentId(
//         Long agentId,
//         OrderStatus orderStatus,
//         Long offset,
//         Long limit
//     );

//     @Query(value = "SELECT * FROM Orders WHERE customer_id = ?1 AND status = ?2 ORDER BY created_at DESC LIMIT ?4 OFFSET ?3", nativeQuery = true)
//     Page<Order> findAllCustomOrdersByCustomerId(
//         Long customerId,
//         OrderStatus orderStatus,
//         Long offset,
//         Long limit
//     );
// }



package com.logistic.logistic_engine.repository;

import com.logistic.logistic_engine.entity.Order;
import com.logistic.logistic_engine.enums.OrderStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // --- Public Facing Methods (Keeps old files from breaking) ---

    default Page<Order> findAllCustomOrdersByAgentId(
        Long agentId,
        OrderStatus orderStatus,
        Long offset,
        Long limit
    ) {
        String statusStr = orderStatus != null ? orderStatus.name() : null;
        List<Order> content = _internalFindAllCustomOrdersByAgentId(agentId, statusStr, offset, limit);
        long total = _internalCountAllCustomOrdersByAgentId(agentId, statusStr);
        
        int pageSize = limit > 0 ? limit.intValue() : 10;
        int pageNum = offset.intValue() / pageSize;
        return new PageImpl<>(content, PageRequest.of(pageNum, pageSize), total);
    }

    default Page<Order> findAllCustomOrdersByCustomerId(
        Long customerId,
        OrderStatus orderStatus,
        Long offset,
        Long limit
    ) {
        String statusStr = orderStatus != null ? orderStatus.name() : null;
        List<Order> content = _internalFindAllCustomOrdersByCustomerId(customerId, statusStr, offset, limit);
        long total = _internalCountAllCustomOrdersByCustomerId(customerId, statusStr);
        
        int pageSize = limit > 0 ? limit.intValue() : 10;
        int pageNum = offset.intValue() / pageSize;
        return new PageImpl<>(content, PageRequest.of(pageNum, pageSize), total);
    }

    // --- Internal Hidden Queries (Uses Named Params to satisfy VS Code) ---
    @Query(value = "SELECT * FROM Orders WHERE agent_id = :agentId AND status = :status ORDER BY created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Order> _internalFindAllCustomOrdersByAgentId(
        @Param("agentId") Long agentId,
        @Param("status") String status,
        @Param("offset") Long offset,
        @Param("limit") Long limit
    );

    @Query(value = "SELECT count(*) FROM Orders WHERE agent_id = :agentId AND status = :status", nativeQuery = true)
    long _internalCountAllCustomOrdersByAgentId(
        @Param("agentId") Long agentId,
        @Param("status") String status
    );

    @Query(value = "SELECT * FROM Orders WHERE customer_id = :customerId AND status = :status ORDER BY created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Order> _internalFindAllCustomOrdersByCustomerId(
        @Param("customerId") Long customerId,
        @Param("status") String status,
        @Param("offset") Long offset,
        @Param("limit") Long limit
    );

    @Query(value = "SELECT count(*) FROM Orders WHERE customer_id = :customerId AND status = :status", nativeQuery = true)
    long _internalCountAllCustomOrdersByCustomerId(
        @Param("customerId") Long customerId,
        @Param("status") String status
    );

    Optional<Order> findById(Long id);

    @Query("SELECT o.agentId FROM Order o WHERE o.id = :orderId")
    Long findAgentIdByOrderId(@Param("orderId") Long orderId);
}