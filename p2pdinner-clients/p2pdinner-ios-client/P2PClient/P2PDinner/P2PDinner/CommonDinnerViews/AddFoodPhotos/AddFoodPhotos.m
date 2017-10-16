//
//  AddFoodPhotos.m
//  P2PDinner
//
//  Created by Selvam M on 3/29/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "AddFoodPhotos.h"
#import "AFImageRequestOperation.h"
#import "SellerHistoryHandler.h"
#import "ItemDetailsShared.h"
#import "StringConstants.h"

#import "LoginServiceHandler.h"
#import "SharedLogin.h"
#import <AVFoundation/AVFoundation.h>

@interface AddFoodPhotos (){
    NSMutableArray *imageUrls;
}
@end
@implementation AddFoodPhotos
@synthesize itemDetails;
-(void)navigationBarsetup{
    UIColor *navBarColor=[UIColor colorWithRed:237.0/255.0
                                         green:134.0/255.0
                                          blue:0.0/255.0
                                         alpha:1];
    NSDictionary *navbarTitleTextAttributes = [NSDictionary dictionaryWithObjectsAndKeys:
                                               [UIColor whiteColor],UITextAttributeTextColor,
                                               [UIFont fontWithName:@"Plantin" size:24], NSFontAttributeName,[NSValue valueWithUIOffset:UIOffsetMake(-1, 0)],UITextAttributeTextShadowOffset, nil];
    
    
    
    [self.navigationController.navigationBar  setTitleTextAttributes:navbarTitleTextAttributes];
    [self.navigationController.navigationBar setBarTintColor:navBarColor];
}

- (void)backAction{
    if (_isFromSettings) {
        [self updateProfileCertificate];
        [self.navigationController popViewControllerAnimated:YES];
    }
    else{
        [self dismissViewControllerAnimated:NO completion:^{
            _completionAction(true);
        }];
        
    }
}
- (void)makeImageURLfromimageName:(NSArray *)imageNames
{
    NSMutableArray *mutableImageURL=[NSMutableArray arrayWithArray:imageNames];
    for (int i=0; i<[imageNames count]; i++) {
        NSString *imageURL=[imageNames objectAtIndex:i];
        imageURL=[imageURL stringByReplacingOccurrencesOfString:kSlash_String withString:kEmpty_String];
        [mutableImageURL replaceObjectAtIndex:i withObject:imageURL];
    
    }
    imageURLArray=nil;
    imageURLArray=[NSArray arrayWithArray:mutableImageURL];
    
}
- (void)viewDidLoad {
    if (_isFromFoodSafty) {
         self.title=kSafetyProfile;
    }
    else{
    self.title=kDinner_Listing;
    }
    imageURLArray=[itemDetails.imageUri componentsSeparatedByString:kComa_String];
    if (imageURLArray.count>=1) {
        NSMutableArray *removeEmptyObject=[NSMutableArray arrayWithArray:imageURLArray];
        [removeEmptyObject removeObject:kEmpty_String];
        imageURLArray=[NSArray arrayWithArray:removeEmptyObject];
    }
    imageURLMutableArray=[NSMutableArray arrayWithArray:imageURLArray];
    if ([imageURLMutableArray count]==0) {
        imageURLArray=[[NSArray alloc]init];
    }
    else
    {
        [self makeImageURLfromimageName:imageURLArray];
    }
    
    if (_isFromSettings && _isFromFoodSafty){
        [self navigationBarsetup];
        CGRect tableviewFrame = photoSelectTableView.frame;
        [photoSelectTableView setFrame:CGRectMake(tableviewFrame.origin.x, tableviewFrame.origin.y, tableviewFrame.size.width, self.view.frame.size.height)];
        [_updateSaftyBtn setHidden:YES];
        
    }
    else if (_isFromFoodSafty){
        [self navigationBarsetup];
        [self navigationBarsetup];
        [_updateSaftyBtn setHidden:NO];
        [_updateSaftyBtn setTitle:@"Go Sell" forState:UIControlStateNormal];
        if(imageURLArray.count > 0){
            _updateSaftyBtn.enabled=YES;
            [_updateSaftyBtn setBackgroundColor:[UIColor colorWithRed:249.0f/255.0f green:221.0f/255.0f blue:129.0f/255.0f alpha:1.0]];
        }
        else{
            _updateSaftyBtn.enabled=NO;
            [_updateSaftyBtn setBackgroundColor:[UIColor lightGrayColor]];
        }
    }
    else{
        [_updateSaftyBtn setHidden:YES];
    }
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}
- (void)completionAction:(void (^)(bool))completion{
    _completionAction=completion;
}
- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    //[photoSelectTableView reloadData];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma UISetupDinnerView
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    //tableView.sectionHeaderHeight=100;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, 58)];
    /* Create custom view to display section header... */
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 30, tableView.frame.size.width, 18)];
    [label setTextColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    [label setFont:[UIFont fontWithName:kFont_Name size:18]];
    NSString *string=kPhotos;
    /* Section header is in 0th index... */
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]]; //your background color...
    return view;

}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    // This will create a "invisible" footer
    return 0.01f;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 58.0f;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return 3;
}

-(void)imageSetForButton:(UIButton *)imgButton withCloseButtonEnable:(UIButton *)closeButton withImageURL:(NSString *)imgURL{
    [imgButton setTitle:kLoading forState:UIControlStateNormal];
    [self imageRequestOperation:imgURL completionBlock:^(UIImage *img, NSError *err){
        if (!err) {
            [imgButton setTitle:kEmpty_String forState:UIControlStateNormal];
            [imgButton setBackgroundImage:img forState:UIControlStateNormal];
            [self checkEnableButton:imgButton closeBtn:closeButton];
        }
        
    }];
    
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row==0) {
        UITableViewCell *cell=(UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:kNoteCell];
        cell.selectionStyle =UITableViewCellSelectionStyleNone;
        if (_isFromFoodSafty) {
            UILabel *noteLable = [cell viewWithTag:666];
            [noteLable setTextColor:[UIColor blackColor]];
            noteLable.text = kfoodSaftyNote;
        }
        return cell;
    }
    SelectPhotoCell *cell;
    cell= (SelectPhotoCell *)[tableView dequeueReusableCellWithIdentifier:kAddPhotoCell];
    
    if (indexPath.row==1) {
        UIButton *imgButton1=(UIButton *)[cell viewWithTag:11];
        UIButton *closeButton1=(UIButton *)[cell viewWithTag:1];
        if (imageURLArray.count>0) {
            [self imageSetForButton:imgButton1 withCloseButtonEnable:closeButton1 withImageURL:[imageURLArray objectAtIndex:0]];
        }
        else{
            [self checkEnableButton:imgButton1 closeBtn:closeButton1];
        }
        UIButton *imgButton2=(UIButton *)[cell viewWithTag:22];
        UIButton *closeButton2=(UIButton *)[cell viewWithTag:2];
        if (imageURLArray.count>1) {
            [self imageSetForButton:imgButton2 withCloseButtonEnable:closeButton2 withImageURL:[imageURLArray objectAtIndex:1]];
        }
        else{
            [self checkEnableButton:imgButton2 closeBtn:closeButton2];
        }
        [[imgButton2 imageView] setContentMode: UIViewContentModeScaleAspectFit];
        [[imgButton1 imageView] setContentMode: UIViewContentModeScaleAspectFit];
        [imgButton1 setContentMode:UIViewContentModeScaleAspectFit];
        [imgButton2 setContentMode:UIViewContentModeScaleAspectFit];

    }
    if (indexPath.row==2) {
        UIButton *imgButton1=(UIButton *)[cell viewWithTag:11];
        UIButton *closeButton1=(UIButton *)[cell viewWithTag:1];
        if (imageURLArray.count>2) {
            [self imageSetForButton:imgButton1 withCloseButtonEnable:closeButton1 withImageURL:[imageURLArray objectAtIndex:2]];
        }else
        {
            [self checkEnableButton:imgButton1 closeBtn:closeButton1];
        }
        
        UIButton *imgButton2=(UIButton *)[cell viewWithTag:22];
        UIButton *closeButton2=(UIButton *)[cell viewWithTag:2];
        if (imageURLArray.count>3) {
            [self imageSetForButton:imgButton2 withCloseButtonEnable:closeButton2 withImageURL:[imageURLArray objectAtIndex:3]];
        }else
        {
            [self checkEnableButton:imgButton2 closeBtn:closeButton2];
        }
        [[imgButton2 imageView] setContentMode: UIViewContentModeScaleAspectFit];
        [[imgButton1 imageView] setContentMode: UIViewContentModeScaleAspectFit];
        [imgButton1 setContentMode:UIViewContentModeScaleAspectFit];
        [imgButton2 setContentMode:UIViewContentModeScaleAspectFit];

    }
    
    
    cell.delegate=self;
    cell.selectionStyle =UITableViewCellSelectionStyleNone;
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0) {
        return 43;
    }
    return 170;
}

- (void)checkEnableButton:(UIButton *)imageButton closeBtn:(UIButton *)closeBtn{
    if ([[imageButton backgroundImageForState:UIControlStateNormal] isEqual:[UIImage imageNamed:kAddPhotoImageName]]) {
        closeBtn.hidden=YES;
    }else
        closeBtn.hidden=NO;
}

- (void)imageRequestOperation:(NSString *)photoUrl completionBlock:(void(^)(UIImage *,NSError *))completion{
    
    // download the photo
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:photoUrl]];
    [request setAccessibilityLabel:kselvam];
    AFImageRequestOperation *operation = [AFImageRequestOperation imageRequestOperationWithRequest:request imageProcessingBlock:^UIImage *(UIImage *image) {
        
        return image;
        
    } success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image) {
        //NSLog(@"%@",request.accessibilityLabel);
        completion(image,nil);
        
    } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error) {
        completion(nil,error);
    }];
    [operation start];
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */
#pragma mark SelectPhotoDelegate

- (void)selectPhotoAction:(id)sender{
    photoButton=(UIButton*)sender;
    [[photoButton imageView] setContentMode:UIViewContentModeScaleAspectFit];
    [photoButton setContentMode:UIViewContentModeScaleAspectFit];
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:kAdd_Dinner_From
                                                             delegate:self
                                                    cancelButtonTitle:kCancel
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:kCamera, kChoose_Photos, nil];
    
    [actionSheet showInView:self.view];
}

#pragma mark -
#pragma mark UIActionSheetDelegate
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    UIImagePickerController * picker = [[UIImagePickerController alloc] init] ;
    picker.navigationBar.translucent = false;
    UIColor *navBarColor=[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1];
    NSDictionary *navbarTitleTextAttributes = [NSDictionary dictionaryWithObjectsAndKeys:
                                               [UIColor whiteColor],UITextAttributeTextColor,
                                               [UIFont fontWithName:kFont_Name size:24], NSFontAttributeName,[NSValue valueWithUIOffset:UIOffsetMake(-1, 0)],UITextAttributeTextShadowOffset, nil];
    [picker.navigationBar  setTitleTextAttributes:navbarTitleTextAttributes];
    [picker.navigationBar setBarTintColor:navBarColor];
    [picker.navigationBar setTintColor:[UIColor whiteColor]];
    
    int i = (int)buttonIndex;
    switch(i)
    {
        case 0:
        {
            if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
            {
                AVAuthorizationStatus status = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
                
                if(status == AVAuthorizationStatusAuthorized) {
                    picker.delegate = self;
                    picker.sourceType = UIImagePickerControllerSourceTypeCamera;
                    [self presentViewController:picker animated:YES completion:^{}];
                } else if(status == AVAuthorizationStatusDenied){
                    [self cameraAccessErrorMessage];
                } else if(status == AVAuthorizationStatusRestricted){
                    [self cameraAccessErrorMessage];
                } else if(status == AVAuthorizationStatusNotDetermined){
                    // not determined
                    [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:^(BOOL granted) {
                        if(granted){
                            picker.delegate = self;
                            picker.sourceType = UIImagePickerControllerSourceTypeCamera;
                            [self presentViewController:picker animated:YES completion:^{}];
                            
                        } else {
                            [self cameraAccessErrorMessage];
                        }
                    }];
                }
            }
        }
            break;
        case 1:
        {
            picker.delegate = self;
            picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
            [self presentViewController:picker animated:YES completion:^{}];
            
        }
        default:
            // Do Nothing.........
            break;
    }
}

-(void)cameraAccessErrorMessage{
    UIAlertView *aleartView=[[UIAlertView alloc]initWithTitle:@"Access Error" message:@"P2P try to access take photo.  Please enable Settings->Privacy->Camera." delegate:nil cancelButtonTitle:kOK otherButtonTitles:nil];
    [aleartView show];
}

#pragma mark -
#pragma - mark Selecting Image from Camera and Library
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    if (!imageUrls) {
        imageUrls=[[NSMutableArray alloc]init];
    }
    if(photoSelectTableView.frame.origin.y!=0)
    {
        [photoSelectTableView setFrame:CGRectMake(photoSelectTableView.frame.origin.x, 0, photoSelectTableView.frame.size.width, photoSelectTableView.frame.size.height)];
    }
    // Picking Image from Camera/ Library
    [picker dismissViewControllerAnimated:YES completion:^{}];
    UIImage *img=[info objectForKey:kImagePicker];
    [photoButton setBackgroundColor:[UIColor clearColor]];
    [photoButton setTitleShadowColor:[UIColor blackColor] forState:UIControlStateNormal];
    [photoButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [photoButton setTitle:kUploading forState:UIControlStateNormal];
    [[SellerHistoryHandler sharedSellerHistoryHandler] photoUpload:img imageTag:1 buttonValue:photoButton itemDetails:itemDetails responceCallBack:^(NSError *error, NSString *imageString) {
        
        if (!error) {
            [imageUrls insertObject:imageString atIndex:0];
            [imageURLMutableArray insertObject:imageString atIndex:0];
            imageURLArray=[NSArray arrayWithArray:imageURLMutableArray];
           if (!_isFromSettings && !_isFromFoodSafty){
                [self.itemDetails setImageUri:[imageURLArray componentsJoinedByString:kComa_String]];
           }else{
               [self enableSellButton];
           }
            
        }
        else{
            UIImage *img;
                img=[UIImage imageNamed:kAddPhotoImageName];
                photoButton.userInteractionEnabled=NO;
            [photoButton setBackgroundImage:img forState:UIControlStateNormal];
            [self tableviewReLoad];
            UIAlertView *aleartView=[[UIAlertView alloc]initWithTitle:kUploadError message:[error.userInfo description] delegate:nil cancelButtonTitle:kOK otherButtonTitles:nil];
            [aleartView show];
        }
    }];
    [photoButton setBackgroundImage:img forState:UIControlStateNormal];
    [photoSelectTableView reloadData];
}
-(void)enableSellButton{
    if (imageURLArray.count>0) {
        [_updateSaftyBtn setEnabled:YES];
        [_updateSaftyBtn setBackgroundColor:[UIColor colorWithRed:249.0f/255.0f green:221.0f/255.0f blue:129.0f/255.0f alpha:1.0]];
    }else{
        [_updateSaftyBtn setEnabled:NO];
        [_updateSaftyBtn setBackgroundColor:[UIColor lightGrayColor]];
    }
}
- (void)tableviewReLoad{
    [photoSelectTableView reloadData];
}

- (void)removePhotoOf:(UITableViewCell *)cell atTag:(NSUInteger)tag{
    NSIndexPath *indexPath = [photoSelectTableView indexPathForCell:cell];
    [self removePhotoFormIndex:indexPath.row atTag:tag];
    //NSLog(@"row %ld tag %lu",(long)indexPath.row,(unsigned long)tag);
}

- (void)removePhotoFormIndex:(NSUInteger)row atTag:(NSUInteger)tag{
    int indexValue=[self arrayObjectAtIndex:row atTag:tag];
    if([imageURLMutableArray count]>=1){
        [imageURLMutableArray removeObjectAtIndex:indexValue];
        itemDetails.imageUri=[imageURLMutableArray componentsJoinedByString:kComa_String];
        imageURLArray=[NSArray arrayWithArray:imageURLMutableArray];
        
    }
    else if(([imageURLMutableArray count]==1) &&([[imageURLMutableArray objectAtIndex:0] length]>1)){
        itemDetails.imageUri=kEmpty_String;
    }
}

- (int)arrayObjectAtIndex:(NSUInteger)row atTag:(NSUInteger)tag{
    NSUInteger arrayCount=imageURLMutableArray.count;
    
    if (row==1&&tag==1) {
        return 0;
    }
    else if (row==1&&tag==2) {
        if (arrayCount==1) {
            return 0;
        }
        return 1;
    }
    else if (row == 2&&tag==1) {
        if (arrayCount==2) {
            return 1;
        }
        return 2;
    }
    else if (row ==2&&tag==2) {
        if (arrayCount==3) {
            return 2;
        }
        return 3;
    }
    return 0;
}
- (IBAction)updateImageToProfile{
    [self updateProfileCertificate];
    if (_isFromFoodSafty){
        if (imageURLArray.count>0) {
            [self dismissViewControllerAnimated:YES completion:^{
            }];
        }
    }
    else{
        [self.navigationController popViewControllerAnimated:YES];
    }
}

- (void)updateProfileCertificate{
    [[LoginServiceHandler sharedServiceHandler] profileUpdateCertificate:[imageURLArray componentsJoinedByString:kComa_String] serviceCallBack:^(NSError *error, LoginResponce *response) {
        if (error) {
            UIAlertView *aleartView=[[UIAlertView alloc]initWithTitle:kUploadError message:kCertUploadError delegate:nil cancelButtonTitle:kOK otherButtonTitles:nil];
            [aleartView show];
        }else{
            [[SharedLogin sharedLogin] setUserCertificates:[imageURLArray componentsJoinedByString:kComa_String]];
        }
    }];
}
@end
