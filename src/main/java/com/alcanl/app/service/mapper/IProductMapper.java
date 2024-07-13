package com.alcanl.app.service.mapper;


import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.service.dto.ProductDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "ProductMapperImpl", componentModel = "spring")
public interface IProductMapper {

    Product productDTOToProduct(ProductDTO productDTO);

    ProductDTO productToProductDTO(Product product);
}
