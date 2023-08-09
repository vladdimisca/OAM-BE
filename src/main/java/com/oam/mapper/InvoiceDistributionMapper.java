package com.oam.mapper;

import com.oam.dto.InvoiceDistributionResponseDto;
import com.oam.dto.SimplifiedPaymentResponseDto;
import com.oam.model.InvoiceDistribution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ApartmentMapper.class, InvoiceMapper.class })
public interface InvoiceDistributionMapper {

    @Mapping(target = "payment", expression = "java(getPayment(invoiceDistribution))")
    InvoiceDistributionResponseDto mapToDto(InvoiceDistribution invoiceDistribution);

    default SimplifiedPaymentResponseDto getPayment(InvoiceDistribution invoiceDistribution) {
        return invoiceDistribution.getPayment() != null ?
                new SimplifiedPaymentResponseDto(invoiceDistribution.getId(),
                        invoiceDistribution.getPayment().getStatus())
                : null;
    }
}
