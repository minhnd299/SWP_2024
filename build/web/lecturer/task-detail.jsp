<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>

    .status {
        font-weight: bold;
    }
    .status.in-progress {
        color: orange;
    }
    .status.done {
        color: green;
    }
</style>
<jsp:include page="header.jsp"></jsp:include>
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
                     <jsp:include page="header-content.jsp"></jsp:include>

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Task Detail</h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">

                    <c:if test="${not empty sessionScope.notification}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert" style="text-align: center">
                            ${sessionScope.notification}
                            <button type="button" class="btn-danger" data-dismiss="alert" aria-label="Close">x</button>
                        </div>
                        <%
                            // Clear the notification after displaying it
                            session.removeAttribute("notification");
                        %>
                    </c:if>
                    <c:if test="${not empty sessionScope.notificationErr}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert"  style="text-align: center">
                            ${sessionScope.notificationErr}
                            <button type="button" class="btn-danger" data-dismiss="alert" >x</button>
                        </div>
                        <%
                            // Clear the notification after displaying it
                            session.removeAttribute("notificationErr");
                        %>
                    </c:if>

                </div>
                <div class="card-body">
                    Title : <span>${task.title}</span> 
                    <br>
                    <br>
                    Description: <span>${task.description}</span> 
                    <br>
                    <br>
                    Status: <span class="status">${task.status}</span>
                    <br>
                    <br>
                    Comment
                    <br>
                    <form action="task-detail" method="POST">
                        <textarea name="comment" class="form-control"></textarea>
                        <input type="hidden" name="action" value="add-comment">
                        <input type="hidden" name="tid" value="${param.id}">
                        <hr>
                        <button class="btn btn-success btn-sm"> Add comment</button>
                    </form>
                    <hr>
                    <br>
                    <c:forEach var="c" items="${comments}">

                        <c:choose>
                            <c:when test="${c.lecturer != null}">
                                <span style="color: orange; font-weight: bold">
                                    ${c.lecturer.fullName} (Lecturer)
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span style="font-weight: bold">
                                    ${c.student.fullName} (Student)
                                </span>
                            </c:otherwise>
                        </c:choose>
                        </span>
                        : ${c.comment}  <c:if test="${account.getId() == c.lecturer.accountLecturer.id}">
                            <i class="fas fa-pen" style="margin-left: 10px; cursor: pointer;" data-toggle="modal" data-target="#editCommentModal${c.comment_id}"></i>
                            <div class="modal fade" id="editCommentModal${c.comment_id}" tabindex="-1" role="dialog" aria-labelledby="editCommentModalLabel${c.comment_id}" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="editCommentModalLabel${c.comment_id}">Edit Comment</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <form action="task-detail" method="POSt">

                                            <div class="modal-body">
                                                <!-- Text area for editing comment -->
                                                <input type="hidden" name="action" value="edit-comment">
                                                <input type="hidden" name="cid" value="${c.comment_id}">
                                                <input type="hidden" name="tid" value="${param.id}">
                                                <textarea name="comment" class="form-control">${c.comment}</textarea>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                <button type="submit" class="btn btn-success">Save changes</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <span style="float: right">${c.time}</span>
                        <hr>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

</div>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        var statusElement = document.querySelector(".status");
        if (statusElement) {
            var statusText = statusElement.textContent.trim().toLowerCase();
            if (statusText === "in progress") {
                statusElement.classList.add("in-progress");
            } else if (statusText === "done") {
                statusElement.classList.add("done");
            }
        }
    });
</script>
<jsp:include page="footer.jsp"></jsp:include>
