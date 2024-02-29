/************************************************************************************
 * Copyright (C) 2018-present E.R.P. Consultores y Asociados, C.A.                  *
 * Contributor(s): Edwin Betancourt, EdwinBetanc0urt@outlook.com                    *
 * This program is free software: you can redistribute it and/or modify             *
 * it under the terms of the GNU General Public License as published by             *
 * the Free Software Foundation, either version 2 of the License, or                *
 * (at your option) any later version.                                              *
 * This program is distributed in the hope that it will be useful,                  *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                     *
 * GNU General Public License for more details.                                     *
 * You should have received a copy of the GNU General Public License                *
 * along with this program. If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/
package org.spin.grpc.service.field.product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.adempiere.core.domains.models.I_M_AttributeSet;
import org.adempiere.core.domains.models.I_M_AttributeSetInstance;
import org.adempiere.core.domains.models.I_M_PriceList_Version;
import org.adempiere.core.domains.models.I_M_Product;
import org.adempiere.core.domains.models.I_M_Product_Category;
import org.adempiere.core.domains.models.I_M_Product_Class;
import org.adempiere.core.domains.models.I_M_Product_PO;
import org.adempiere.core.domains.models.I_M_Warehouse;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MLookupInfo;
import org.compiere.model.MProduct;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.spin.backend.grpc.common.ListLookupItemsResponse;
import org.spin.backend.grpc.field.product.ListAttributeSetInstancesRequest;
import org.spin.backend.grpc.field.product.ListAttributeSetsRequest;
import org.spin.backend.grpc.field.product.ListAvailableToPromisesRequest;
import org.spin.backend.grpc.field.product.ListAvailableToPromisesResponse;
import org.spin.backend.grpc.field.product.ListPricesListVersionsRequest;
import org.spin.backend.grpc.field.product.ListProductCategoriesRequest;
import org.spin.backend.grpc.field.product.ListProductClasessRequest;
import org.spin.backend.grpc.field.product.ListProductGroupsRequest;
import org.spin.backend.grpc.field.product.ListProductsInfoRequest;
import org.spin.backend.grpc.field.product.ListProductsInfoResponse;
import org.spin.backend.grpc.field.product.ListRelatedProductsRequest;
import org.spin.backend.grpc.field.product.ListRelatedProductsResponse;
import org.spin.backend.grpc.field.product.ListSubstituteProductsRequest;
import org.spin.backend.grpc.field.product.ListSubstituteProductsResponse;
import org.spin.backend.grpc.field.product.ListVendorPurchasesRequest;
import org.spin.backend.grpc.field.product.ListVendorPurchasesResponse;
import org.spin.backend.grpc.field.product.ListVendorsRequest;
import org.spin.backend.grpc.field.product.ListWarehouseStocksRequest;
import org.spin.backend.grpc.field.product.ListWarehouseStocksResponse;
import org.spin.backend.grpc.field.product.ListWarehousesRequest;
import org.spin.backend.grpc.field.product.ProductInfo;
import org.spin.base.db.WhereClauseUtil;
import org.spin.base.util.ContextManager;
import org.spin.base.util.ReferenceInfo;
import org.spin.grpc.service.UserInterface;
import org.spin.service.grpc.authentication.SessionManager;
import org.spin.service.grpc.util.db.CountUtil;
import org.spin.service.grpc.util.db.LimitUtil;
import org.spin.service.grpc.util.db.ParameterUtil;
import org.spin.service.grpc.util.value.BooleanManager;
import org.spin.service.grpc.util.value.ValueManager;

public class ProductInfoLogic {

	public static String tableName = I_M_Product.Table_Name;


	public static ListLookupItemsResponse.Builder listWarehouses(ListWarehousesRequest request) {
		// Warehouse
		MLookupInfo reference = ReferenceInfo.getInfoFromRequest(
			DisplayType.TableDir,
			0, 0, 0,
			0,
			I_M_Warehouse.COLUMNNAME_M_Warehouse_ID, I_M_Warehouse.Table_Name
		);

		ListLookupItemsResponse.Builder builderList = UserInterface.listLookupItems(
			reference,
			null,
			request.getPageSize(),
			request.getPageToken(),
			request.getSearchValue(),
			true
		);

		return builderList;
	}

	public static ListLookupItemsResponse.Builder listPricesListVersions(ListPricesListVersionsRequest request) {
		// Warehouse
		MLookupInfo reference = ReferenceInfo.getInfoFromRequest(
			DisplayType.TableDir,
			0, 0, 0,
			0,
			I_M_PriceList_Version.COLUMNNAME_M_PriceList_Version_ID, I_M_PriceList_Version.Table_Name
		);

		ListLookupItemsResponse.Builder builderList = UserInterface.listLookupItems(
			reference,
			null,
			request.getPageSize(),
			request.getPageToken(),
			request.getSearchValue(),
			true
		);

		return builderList;
	}

	public static ListLookupItemsResponse.Builder listAttributeSets(ListAttributeSetsRequest request) {
		// Warehouse
		MLookupInfo reference = ReferenceInfo.getInfoFromRequest(
			DisplayType.TableDir,
			0, 0, 0,
			0,
			I_M_AttributeSet.COLUMNNAME_M_AttributeSet_ID, I_M_AttributeSet.Table_Name
		);

		ListLookupItemsResponse.Builder builderList = UserInterface.listLookupItems(
			reference,
			null,
			request.getPageSize(),
			request.getPageToken(),
			request.getSearchValue(),
			true
		);

		return builderList;
	}

	public static ListLookupItemsResponse.Builder listAttributeSetInstances(ListAttributeSetInstancesRequest request) {
		// Warehouse
		MLookupInfo reference = ReferenceInfo.getInfoFromRequest(
			DisplayType.PAttribute,
			0, 0, 0,
			0,
			I_M_AttributeSetInstance.COLUMNNAME_M_AttributeSetInstance_ID, I_M_AttributeSetInstance.Table_Name
		);

		ListLookupItemsResponse.Builder builderList = UserInterface.listLookupItems(
			reference,
			null,
			request.getPageSize(),
			request.getPageToken(),
			request.getSearchValue(),
			true
		);

		return builderList;
	}

	public static ListLookupItemsResponse.Builder listProductCategories(ListProductCategoriesRequest request) {
		// Warehouse
		MLookupInfo reference = ReferenceInfo.getInfoFromRequest(
			DisplayType.TableDir,
			0, 0, 0,
			0,
			I_M_Product_Category.COLUMNNAME_M_Product_Category_ID, I_M_Product_Category.Table_Name
		);

		ListLookupItemsResponse.Builder builderList = UserInterface.listLookupItems(
			reference,
			null,
			request.getPageSize(),
			request.getPageToken(),
			request.getSearchValue(),
			true
		);

		return builderList;
	}

	public static ListLookupItemsResponse.Builder listProductGroups(ListProductGroupsRequest request) {
		// Warehouse
		MLookupInfo reference = ReferenceInfo.getInfoFromRequest(
			DisplayType.TableDir,
			0, 0, 0,
			0,
			I_M_Product_Category.COLUMNNAME_M_Product_Category_ID, I_M_Product_Category.Table_Name
		);

		ListLookupItemsResponse.Builder builderList = UserInterface.listLookupItems(
			reference,
			null,
			request.getPageSize(),
			request.getPageToken(),
			request.getSearchValue(),
			true
		);

		return builderList;
	}

	public static ListLookupItemsResponse.Builder listProductClasses(ListProductClasessRequest request) {
		// Warehouse
		MLookupInfo reference = ReferenceInfo.getInfoFromRequest(
			DisplayType.TableDir,
			0, 0, 0,
			0,
			I_M_Product_Class.COLUMNNAME_M_Product_Class_ID, I_M_Product_Class.Table_Name
		);

		ListLookupItemsResponse.Builder builderList = UserInterface.listLookupItems(
			reference,
			null,
			request.getPageSize(),
			request.getPageToken(),
			request.getSearchValue(),
			true
		);

		return builderList;
	}

	public static ListLookupItemsResponse.Builder listVendors(ListVendorsRequest request) {
		// Warehouse
		MLookupInfo reference = ReferenceInfo.getInfoFromRequest(
			DisplayType.Search,
			0, 0, 0,
			0,
			I_M_Product_PO.COLUMNNAME_C_BPartner_ID, I_M_Product_PO.Table_Name
		);

		ListLookupItemsResponse.Builder builderList = UserInterface.listLookupItems(
			reference,
			null,
			request.getPageSize(),
			request.getPageToken(),
			request.getSearchValue(),
			true
		);

		return builderList;
	}



	public static ListProductsInfoResponse.Builder listProductsInfo(ListProductsInfoRequest request) {
		MLookupInfo reference = ReferenceInfo.getInfoFromRequest(
			request.getReferenceId(),
			request.getFieldId(),
			request.getProcessParameterId(),
			request.getBrowseFieldId(),
			request.getColumnId(),
			request.getColumnName(),
			request.getTableName()
		);

		//  Fill Cintext
		Properties context = Env.getCtx();
		int windowNo = ThreadLocalRandom.current().nextInt(1, 8996 + 1);
		ContextManager.setContextWithAttributesFromString(windowNo, context, request.getContextAttributes());

		StringBuffer whereClause = new StringBuffer("1=1");
		// validation code of field
		String validationCode = WhereClauseUtil.getWhereRestrictionsWithAlias(tableName, reference.ValidationCode);
		String parsedValidationCode = Env.parseContext(context, windowNo, validationCode, false);
		if (!Util.isEmpty(reference.ValidationCode, true)) {
			if (Util.isEmpty(parsedValidationCode, true)) {
				throw new AdempiereException("@WhereClause@ @Unparseable@");
			}
			whereClause.append(" AND ").append(parsedValidationCode);
		}

		String sqlQuery = "SELECT "
			+ "p.M_Product_ID, p.UUID, " // + "p.Discontinued, "
			+ "pc.Name AS M_Product_Category_ID, pcl.Name AS M_Product_Class_ID, pg.Name AS M_Product_Group_ID, "
			+ "p.Value, p.Name, p.UPC, p.SKU, p.IsActive, u.name AS C_UOM_ID, bp.Name AS Vendor, "
			+ "pa.IsInstanceAttribute AS IsInstanceAttribute "
		;
		String sqlFrom = "FROM M_Product p"
			+ " LEFT OUTER JOIN M_AttributeSet pa ON (p.M_AttributeSet_ID=pa.M_AttributeSet_ID)"
			+ " LEFT OUTER JOIN M_Product_PO ppo ON (p.M_Product_ID=ppo.M_Product_ID AND ppo.IsCurrentVendor='Y' AND ppo.IsActive='Y')"
			+ " LEFT OUTER JOIN M_Product_Category pc ON (p.M_Product_Category_ID=pc.M_Product_Category_ID)"
			+ " LEFT OUTER JOIN M_Product_Class pcl ON (p.M_Product_Class_ID=pcl.M_Product_Class_ID)"
			+ " LEFT OUTER JOIN M_Product_Group pg ON (p.M_Product_Group_ID=pg.M_Product_Group_ID)"
			+ " LEFT OUTER JOIN C_BPartner bp ON (ppo.C_BPartner_ID=bp.C_BPartner_ID)"
			+ " LEFT OUTER JOIN C_UOM u ON (p.C_UOM_ID=u.C_UOM_ID)"
		;

		String sqlWhere = " WHERE p.AD_Client_ID = ? ";

		List<Object> parametersList = new ArrayList<>();
		parametersList.add(
			Env.getAD_Client_ID(context)
		);

		// Value
		if (!Util.isEmpty(request.getValue())) {
			sqlWhere += " AND UPPER(p.Value) LIKE '%' || UPPER(?) || '%' ";
			parametersList.add(
				request.getValue()
			);
		}
		// Name
		if (!Util.isEmpty(request.getName())) {
			sqlWhere += " AND UPPER(p.Name) LIKE '%' || UPPER(?) || '%' ";
			parametersList.add(
				request.getName()
			);
		}
		// UPC/EAN
		if (!Util.isEmpty(request.getUpc())) {
			sqlWhere += " AND UPPER(p.UPC) LIKE '%' || UPPER(?) || '%' ";
			parametersList.add(
				request.getUpc()
			);
		}
		// UPC/EAN
		if (!Util.isEmpty(request.getSku())) {
			sqlWhere += " AND UPPER(p.SKAU) LIKE '%' || UPPER(?) || '%' ";
			parametersList.add(
				request.getSku()
			);
		}
		// Product Category
		if (request.getProductCategoryId() > 0) {
			sqlWhere += " AND p.M_Product_Category_ID = ? ";
			parametersList.add(
				request.getProductCategoryId()
			);
		}
		// Product Group
		if (request.getProductGroupId() > 0) {
			sqlWhere += " AND p.M_Product_Group_ID = ? ";
			parametersList.add(
				request.getProductGroupId()
			);
		}
		// Product Class
		if (request.getProductClassId() > 0) {
			sqlWhere += " AND p.M_Product_Class_ID = ? ";
			parametersList.add(
				request.getProductClassId()
			);
		}
		// Attribute Set
		if (request.getAttributeSetId() > 0) {
			sqlWhere += " AND p.M_AttributeSet_ID = ? ";
			parametersList.add(
				request.getAttributeSetId()
			);
		}
		// Attribute Set Instance
		if (request.getAttributeSetInstanceId() > 0) {
			sqlWhere += " AND p.M_AttributeSetInstance_ID = ? ";
			parametersList.add(
				request.getAttributeSetInstanceId()
			);
		}
		// Is Stocked
		if (!Util.isEmpty(request.getIsStocked())) {
			sqlWhere += " AND p.IsStocked = ? ";
			boolean isStocked = BooleanManager.getBooleanFromString(
				request.getIsStocked()
			);
			parametersList.add(isStocked);
		}

		String sqlOrderBy = "";

		if (request.getPriceListId() > 0) {
			sqlFrom += " LEFT OUTER JOIN ("
				+			"SELECT mpp.M_Product_ID, mpp.M_PriceList_Version_id, "
				+			"mpp.IsActive, mpp.PriceList, mpp.PriceStd, mpp.PriceLimit"
				+			" FROM M_ProductPrice mpp, M_PriceList_Version mplv "
				+			" WHERE mplv.M_PriceList_Version_ID = mpp.M_PriceList_Version_ID AND mplv.IsActive = 'Y'"
				+		") AS pr"
				+ " ON (p.M_Product_ID=pr.M_Product_ID AND pr.IsActive='Y') "
			;

			sqlWhere += " pr.M_PriceList_Version_ID = ? ";
			parametersList.add(request.getPriceListId());
		}

		String sql = sqlQuery + sqlFrom + sqlWhere;

		//	Count records
		int pageNumber = LimitUtil.getPageNumber(SessionManager.getSessionUuid(), request.getPageToken());
		int limit = LimitUtil.getPageSize(request.getPageSize());
		int offset = (pageNumber - 1) * limit;
		int count =  CountUtil.countRecords(sql, tableName, "p", parametersList);
		//	Set page token
		String nexPageToken = null;
		if(LimitUtil.isValidNextPageToken(count, offset, limit)) {
			nexPageToken = LimitUtil.getPagePrefix(SessionManager.getSessionUuid()) + (pageNumber + 1);
		}

		ListProductsInfoResponse.Builder builderList = ListProductsInfoResponse.newBuilder()
			.setRecordCount(count)
			.setNextPageToken(
				ValueManager.validateNull(
					nexPageToken
				)
			)
		;

		String parsedSQL = LimitUtil.getQueryWithLimit(sql, limit, offset);
		//	Add Order By
		parsedSQL = parsedSQL + sqlOrderBy;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(parsedSQL, null);
			ParameterUtil.setParametersFromObjectsList(pstmt, parametersList);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProductInfo.Builder builder = ProductInfo.newBuilder()
					.setId(
						rs.getInt(
							I_M_Product.COLUMNNAME_M_Product_ID
						)
					)
					.setUuid(
						ValueManager.validateNull(
							rs.getString(
								I_M_Product.COLUMNNAME_UUID
							)
						)
					)
					.setValue(
						ValueManager.validateNull(
							rs.getString(
								I_M_Product.COLUMNNAME_Value
							)
						)
					)
					.setName(
						ValueManager.validateNull(
							rs.getString(
								I_M_Product.COLUMNNAME_Name
							)
						)
					)
					.setUpc(
						ValueManager.validateNull(
							rs.getString(
								I_M_Product.COLUMNNAME_UPC
							)
						)
					)
					.setSku(
						ValueManager.validateNull(
							rs.getString(
								I_M_Product.COLUMNNAME_SKU
							)
						)
					)
					.setUom(
						ValueManager.validateNull(
							rs.getString(
								I_M_Product.COLUMNNAME_C_UOM_ID
							)
						)
					)
					.setProductCategory(
						ValueManager.validateNull(
							rs.getString(
								I_M_Product.COLUMNNAME_M_Product_Category_ID
							)
						)
					)
					.setProductClass(
						ValueManager.validateNull(
							rs.getString(
								I_M_Product.COLUMNNAME_M_Product_Class_ID
							)
						)
					)
					.setProductGroup(
						ValueManager.validateNull(
							rs.getString(
								I_M_Product.COLUMNNAME_M_Product_Group_ID
							)
						)
					)
					.setIsInstanceAttribute(
						rs.getBoolean(
							I_M_AttributeSet.COLUMNNAME_IsInstanceAttribute
						)
					)
					.setVendor(
						ValueManager.validateNull(
							rs.getString(
								"Vendor"
							)
						)
					)
					.setIsActive(
						rs.getBoolean(
							I_M_Product.COLUMNNAME_IsActive
						)
					)
				;
				builderList.addRecords(builder);
			}
		} catch (SQLException e) {
			throw new AdempiereException(e);
		} finally {
			DB.close(rs, pstmt);
		}

		return builderList;
	}



	/**
	 * Validate productId and MProduct, and get instance
	 * @param tableName
	 * @return
	 */
	public static MProduct validateAndGetProduct(int productId) {
		if (productId <= 0) {
			throw new AdempiereException("@FillMandatory@ @M_Product_ID@");
		}
		MProduct product = MProduct.get(Env.getCtx(), productId);
		if (product == null || product.getM_Product_ID() <= 0) {
			throw new AdempiereException("@M_Product_ID@ @NotFound@");
		}
		return product;
	}



	public static ListWarehouseStocksResponse.Builder listWarehouseStocks(ListWarehouseStocksRequest request) {
		validateAndGetProduct(
			request.getProductId()
		);

		ListWarehouseStocksResponse.Builder builderList = ListWarehouseStocksResponse.newBuilder();

		return builderList;
	}



	public static ListSubstituteProductsResponse.Builder listSubstituteProducts(ListSubstituteProductsRequest request) {
		validateAndGetProduct(
			request.getProductId()
		);

		ListSubstituteProductsResponse.Builder builderList = ListSubstituteProductsResponse.newBuilder();

		return builderList;
	}



	public static ListRelatedProductsResponse.Builder listRelatedProducts(ListRelatedProductsRequest request) {
		validateAndGetProduct(
			request.getProductId()
		);

		ListRelatedProductsResponse.Builder builderList = ListRelatedProductsResponse.newBuilder();

		return builderList;
	}



	public static ListAvailableToPromisesResponse.Builder listAvailableToPromises(ListAvailableToPromisesRequest request) {
		validateAndGetProduct(
			request.getProductId()
		);

		ListAvailableToPromisesResponse.Builder builderList = ListAvailableToPromisesResponse.newBuilder();

		return builderList;
	}



	public static ListVendorPurchasesResponse.Builder listVendorPurchases(ListVendorPurchasesRequest request) {
		validateAndGetProduct(
			request.getProductId()
		);

		ListVendorPurchasesResponse.Builder builderList = ListVendorPurchasesResponse.newBuilder();

		return builderList;
	}

}
