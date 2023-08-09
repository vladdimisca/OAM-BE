package com.oam.mapper;

import com.oam.dto.InvoiceDistributionResponseDto;
import com.oam.dto.PaymentResponseDto;
import com.oam.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InvoiceDistributionMapper.class, UserMapper.class})
public abstract class PaymentMapper {

    @Autowired
    private InvoiceDistributionMapper invoiceDistributionMapper;


    @Mapping(target = "invoiceDistributions", expression = "java(mapInvoiceDistributions(payment))")
    public abstract PaymentResponseDto mapToDto(Payment payment);

    protected List<InvoiceDistributionResponseDto> mapInvoiceDistributions(Payment payment) {
        return payment.getInvoiceDistributions().stream().map(invoiceDistributionMapper::mapToDto).toList();
    }
}
