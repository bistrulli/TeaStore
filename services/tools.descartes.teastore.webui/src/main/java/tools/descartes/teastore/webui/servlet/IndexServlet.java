/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.descartes.teastore.webui.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.descartes.teastore.registryclient.Service;
import tools.descartes.teastore.registryclient.loadbalancers.LoadBalancerTimeoutException;
import tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedImageOperations;
import tools.descartes.teastore.registryclient.rest.LoadBalancedStoreOperations;
import tools.descartes.teastore.entities.Category;
import tools.descartes.teastore.entities.ImageSizePreset;

/**
 * Servlet implementation for the web view of "Index".
 * 
 * @author Andre Bauer
 */
@WebServlet("/index")
public class IndexServlet extends AbstractUIServlet {

	private static final long serialVersionUID = 1L;
	private static int servletCounter=0;
	private Integer id=null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IndexServlet() {
		super();
		id=servletCounter++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void handleGETRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, LoadBalancerTimeoutException {
		
		//acquire the server thread
		System.out.println("Acquired Ui");
		
		//checking if the cookie is setted
		checkforCookie(request, response);
		//get the category list (Persistence layer calling)
		request.setAttribute("CategoryList",LoadBalancedCRUDOperations.getEntities(Service.PERSISTENCE, "categories", Category.class, -1, -1));
		//check if the iser is logged (call the auth service)
		request.setAttribute("login", LoadBalancedStoreOperations.isLoggedIn(getSessionBlob(request)));
		//get the image from the image service
		request.setAttribute("storeIcon",LoadBalancedImageOperations.getWebImage("icon", ImageSizePreset.ICON.getSize()));
		
		//compute the page (local)
		request.setAttribute("title", "TeaStore Home");
		request.getRequestDispatcher("WEB-INF/pages/index.jsp").forward(request, response);
		
		//release the server thread
		System.out.println("Released Ui");
	}

}
