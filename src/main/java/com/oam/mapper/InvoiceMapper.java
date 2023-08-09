package com.oam.mapper;

import com.oam.dto.CreateInvoiceRequestDto;
import com.oam.dto.InvoiceResponseDto;
import com.oam.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AssociationMapper.class)
public interface InvoiceMapper {

    InvoiceResponseDto mapToDto(Invoice invoice);

    @Mapping(source = "associationId", target = "association.id")
    Invoice mapToEntity(CreateInvoiceRequestDto createInvoiceRequestDto);
}
