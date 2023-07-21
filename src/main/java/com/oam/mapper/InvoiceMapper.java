package com.oam.mapper;

import com.oam.dto.CreateInvoiceRequestDto;
import com.oam.dto.InvoiceResponseDto;
import com.oam.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(source = "association.id", target = "associationId")
    InvoiceResponseDto mapToDto(Invoice invoice);

    @Mapping(source = "associationId", target = "association.id")
    Invoice mapToEntity(CreateInvoiceRequestDto createInvoiceRequestDto);
}
