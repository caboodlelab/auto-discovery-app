package com.caboodle.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import com.caboodle.dao.IGetDBModel;
import com.caboodle.entity.DBEntity;

import io.vertx.core.Future;

/**
 * @author harishchauhan
 *
 */
@Service
public class ApiUtility {

	@Inject
	private IGetDBModel getDbService;

	public Future<String> perform() {
		Future<String> result = Future.future();
		Future<List<Response>> srtList = getData();
		srtList.setHandler(response->{
			if(response.succeeded()){
				result.complete(getHtml(response.result(),null));
			}else{
				result.fail(response.cause());
			}
		});
		
		return result;

	}

	private Future<List<Response>> getData() {
		
		Future<List<Response>> responseFuture = Future.future();
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", 1);
		Future<List<DBEntity>> dataList = getDbService.fetchByFind(paramMap);
		dataList.setHandler(res->{
			
			if(res.succeeded()){
				List<Response> responseList = new ArrayList<>();
				for (DBEntity entity : res.result()) {
					Response response = new Response();
					response.setId(entity.getId()+"");
					response.setName(entity.getName());
					responseList.add(response);
				}
				responseFuture.complete(responseList);
			}else{
				responseFuture.fail("Some Exception Occured");
			}
		});
		
		return responseFuture;
		
	}
	
	
	
	private String getHtml(List<Response>responseList,String [] rowNames) {
		String[] defaultRows = {"employeeId","employeeName"}; 
		if(rowNames == null || rowNames.length == 0){
			rowNames = defaultRows; 
		}
		StringBuilder builder = new StringBuilder();
		builder.append("<html><body style=margin: 0px auto;>"
				+ "<header style=\"text-align: center;padding-bottom: 20px;\">"+"<b>Caboodle reactive microservice application</b>"+"</header>"
				+ "<table style=\"border: 1px solid black; margin: 0px auto; text-align: center; width: 70%;\">");
		builder.append("<tr style=\"background: #f5931e;color: #fff;\">");
		builder.append("<th>");
		for(String rowName: rowNames){
			builder.append("<th>").append(rowName).append("</th>");
		}
		builder.append("</tr>");
		int counter =1;
		for (Response response : responseList) {
				builder.append("<tr style=\"line-height: 17px;background: #CCC3C3;\">");
				builder.append("<td>").append(counter).append("</td>");
				builder.append("<td>").append(response.getId() == null ? "" : response.getId()).append("</td>");
				builder.append("<td>").append(response.getName() == null ? "" : response.getName()).append("</td>");
			builder.append("</tr>");
			counter++;
		}
		
		builder.append("</table></body></html>");
		return builder.toString();
	}

}
